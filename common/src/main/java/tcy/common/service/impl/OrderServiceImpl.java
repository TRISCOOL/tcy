package tcy.common.service.impl;

import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tcy.common.dto.LogisticsResponseDTO;
import tcy.common.exception.ResponseCode;
import tcy.common.exception.TcyException;
import tcy.common.mapper.*;
import tcy.common.model.*;
import tcy.common.model.wx.PayInfo;
import tcy.common.model.wx.PayResponseInfo;
import tcy.common.service.IntegralService;
import tcy.common.service.OrderService;
import tcy.common.service.ShareService;
import tcy.common.utils.DateTimeUtil;
import tcy.common.utils.Utils;
import tcy.common.utils.WxPayUtils;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private CourierCompanyMapper courierCompanyMapper;

    @Autowired
    private ClothingConfigMapper clothingConfigMapper;

    @Autowired
    private ShareService shareService;

    @Autowired
    private IntegralService integralService;

    @Autowired
    private UserMapper userMapper;

    private static String WXXDURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static String LOGISTICS_QUERY = "http://wdexpress.market.alicloudapi.com/gxali";
    private static String LOGISTICS_CODE = "af669cc2334f4d1698bace8101c078ae";

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Override
    @Transactional
    public Order createOrder(Long addressId, List<ShoppingCart> shoppingCarts, Long userId) {

        Order order = new Order();
        order.setAddressId(addressId);
        order.setCreateTime(new Date());
        order.setDelete(0);

        Double sumCost = 0d;
        for (ShoppingCart shoppingCart:shoppingCarts){
            Double cost = getCost(shoppingCart);
            sumCost += cost;
        }

        order.setShouldPayAmount(new BigDecimal(sumCost));
        order.setStatus(0);
        order.setOrderNumber(getRandomNum());
        order.setUserId(userId);

        int result = orderMapper.insertSelective(order);
        if (result <= 0){
            throw new TcyException(ResponseCode.SERVER_ERROR);
        }

        //反写订单id
        updateOrderIdIntoShopcart(order.getId(),shoppingCarts);


        return order;
    }

    @Override
    public Order selectOrderById(Long orderId) {
        return orderMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public Order selectOrderForCallBack(String orderNumber) {
        return orderMapper.selectOrderByOrderNumberAndStatus(orderNumber);
    }

    @Override
    public OrderVo getOrderDetails(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        OrderVo vo = OrderVo.getVo(order);

        List<ProductVo> productVos = shoppingCartMapper.selectProductDetailsWithOrder(orderId);
        vo.setProductVos(productVos);

        Address address = addressMapper.selectByPrimaryKey(order.getAddressId());
        vo.setAddress(address);

        return vo;
    }

    private void updateOrderIdIntoShopcart(Long orderId,List<ShoppingCart> shoppingCarts){
        for (ShoppingCart shoppingCart : shoppingCarts){
            ShoppingCart newShopping = new ShoppingCart();
            newShopping.setId(shoppingCart.getId());
            newShopping.setOrderId(orderId);
            newShopping.setStatus(1);

            int result = shoppingCartMapper.updateByPrimaryKeySelective(newShopping);
            if (result <= 0){
                throw new TcyException(ResponseCode.SERVER_ERROR);
            }
        }
    }

    private String getRandomNum(){
        String nowTime = DateTimeUtil.getYYYYMMDDHHMMSS(new Date());
        return nowTime.replaceAll("-","")
                .replaceAll(":","")
                .replaceAll(" ","");
    }

    private Double getCost(ShoppingCart shoppingCart){
        if (shoppingCart == null)
            return 0d;

        Product product = productMapper.selectProductByClothingConfig(shoppingCart.getProductId());
        if (product == null){
            return 0d;
        }

        checkProdcut(product);

        return product.getRetailPrice().doubleValue() * shoppingCart.getNum();

    }

    private void checkProdcut(Product product){
        if (product.getShelf().equals(0)){
            throw new TcyException(ResponseCode.PRODUCT_SHELF);
        }
    }

    @Override
    public PayResponseInfo confirmPay(Long orderId,String spBillCreateIp) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        User user = userMapper.selectByPrimaryKey(order.getUserId());
        PayInfo payInfo = PayInfo.getPayInfo(order,spBillCreateIp);
        payInfo.setOpenId(user.getOpenId());

        String nonceStr = WxPayUtils.getNonceStr();
        payInfo.setNonceStr(nonceStr);
        String sign = WxPayUtils.getPaySign(payInfo);
        payInfo.setSign(sign);
        payInfo.setSpbillCreateIp(spBillCreateIp);

        String payXmlStr = payXmlStr = getXmlByPayInfo(payInfo);

        PayResponseInfo responseInfo = connectWxPay(payXmlStr,WXXDURL);

        order.setStatus(1);
        orderMapper.updateByPrimaryKeySelective(order);

        return responseInfo;
    }

    private PayResponseInfo connectWxPay(String data,String url){
        CloseableHttpClient httpClient = HttpClients.custom().build();

        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(data,"UTF-8");
        entity.setContentEncoding("UTF-8");
        post.setEntity(entity);

        try {
            CloseableHttpResponse response = httpClient.execute(post);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK){
                String content = EntityUtils.toString(response.getEntity(),"UTF-8");
                logger.info(content);
                PayResponseInfo responseInfo = analysisPaResponseXMl(content);
                if (responseInfo == null)
                    throw new TcyException(ResponseCode.SERVER_ERROR);

                if (responseInfo.getResultCode() != null && responseInfo.getReturnCode() != null &&
                        responseInfo.getReturnCode().equals("SUCCESS") && responseInfo.getResultCode().equals("SUCCESS")){
                    return responseInfo;
                }else {
                    logger.error("wx pay failed cause by {}",content);
                    throw new TcyException();
                }

            }else {
                logger.error("response exception code is {}",code);
                throw new TcyException(ResponseCode.WX_HTTP_ERROR);
            }
        } catch (IOException e) {
            throw new TcyException(ResponseCode.WX_CONNECT_ERROR);
        }
    }

    private PayResponseInfo analysisPaResponseXMl(String xml){
        try {
            org.dom4j.Document document = DocumentHelper.parseText(xml);
            PayResponseInfo payResponseInfo = PayResponseInfo.getPayResponseInfo(document);
            return payResponseInfo;
        } catch (DocumentException e) {
            logger.error("analysis xml failed with {}",xml);
            throw new TcyException(e);
        }
    }

    private String getXmlByPayInfo(PayInfo payInfo){

        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<appid>"+payInfo.getAppId()+"</appid>");
        sb.append("<mch_id>"+payInfo.getMchId()+"</mch_id>");
        sb.append("<nonce_str>"+payInfo.getNonceStr()+"</nonce_str>");
        sb.append("<sign>"+payInfo.getSign()+"</sign>");
        sb.append("<body>"+payInfo.getBody()+"</body>");
        sb.append("<out_trade_no>"+payInfo.getOutTradeNo()+"</out_trade_no>");
        sb.append("<total_fee>"+payInfo.getTotalFee()+"</total_fee>");
        sb.append("<spbill_create_ip>"+payInfo.getSpbillCreateIp()+"</spbill_create_ip>");
        sb.append("<notify_url>"+payInfo.getNotifyUrl()+"</notify_url>");
        sb.append("<trade_type>"+payInfo.getTradeType()+"</trade_type>");
        sb.append("<openid>"+payInfo.getOpenId()+"</openid>");
        sb.append("</xml>");

        return sb.toString();
    }

    @Override
    public List<Order> listOrder(Long userId, Integer type) {

        List<Order> orderList = orderMapper.selectOrdersByStatusAndUser(type,userId);
        orderList.forEach(order -> {
            List<ProductVo> productVos = shoppingCartMapper.selectProductDetailsWithOrder(order.getId());
            order.setProductVoList(productVos);
        });

        return orderList;
    }

    @Override
    @Transactional
    public boolean updateOrderWithPay(Order order) {
        int result = orderMapper.updateByPrimaryKeySelective(order);
        //修改库存
        if (result != 0){
            List<ProductVo> productVos = shoppingCartMapper.selectProductDetailsWithOrder(order.getId());
            for (ProductVo productVo : productVos){
                //更新购物车状态
                updateShoppingCartStatus(productVo);

                //更新具体商品配置库存
                updateCCSellNum(productVo);

                //更新产品库存
                updateProductSellNum(productVo);

                //TODO 更新用户积分
                integralService.updateIntegralForUserByOrder(order);
            }

            //处理分享与积分
            updateShareOperation(order);
            return true;
        }

        return false;
    }

    private void updateShareOperation(Order order){
        List<ProductVo> productVos = shoppingCartMapper.selectProductDetailsWithOrder(order.getId());
        if (productVos != null && productVos.size() > 0){
            for (ProductVo p : productVos){
                List<ShareOperationRecord> shareOperationRecords =
                        shareService.selectOperations(p.getProductId(),order.getUserId(),1);
                if (shareOperationRecords.size() > 0){
                    for (ShareOperationRecord operationRecord : shareOperationRecords){
                        operationRecord.setOperationType(2);
                        shareService.updateShareOperationRecord(operationRecord);
                        //TODO 对分享人的积分进行修改
                        integralService.updateIntegralForUserByShare(operationRecord);

                    }
                }
            }
        }
    }

    @Override
    public boolean updateByPrimaryKeySelective(Order order) {
        int result = orderMapper.updateByPrimaryKeySelective(order);
        if (result != 0){
            return true;
        }

        return false;
    }

    @Override
    public List<Order> listOrderByStatusAndShop(Long shopId, Integer status,Integer offset,Integer length) {
        return orderMapper.listOrderByStatusAndShop(shopId,status,offset,length);
    }

    @Override
    public List<CourierCompany> listCourierCompanies() {
        return courierCompanyMapper.listAll();
    }

    @Override
    public LogisticsResponseDTO logisticsDetails(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order.getWaybillNumber() == null || order.getCourierCompanyId() == null)
            return null;

        CourierCompany courierCompany = courierCompanyMapper.selectByPrimaryKey(order.getCourierCompanyId());
        if (courierCompany != null){
            return queryLogistics(order.getWaybillNumber(),courierCompany.getCode());
        }
        return null;
    }

    private LogisticsResponseDTO queryLogistics(String logisticsNum,String courierCode){

        String url = LOGISTICS_QUERY+"?n="+logisticsNum+"&t="+courierCode;
        try {
            CloseableHttpClient client = HttpClients.custom().build();
            HttpGet get = new HttpGet(url);
            get.setHeader("Authorization", "APPCODE " + LOGISTICS_CODE);

            CloseableHttpResponse response = client.execute(get);
            int code = response.getStatusLine().getStatusCode();
            if (code == HttpStatus.SC_OK){
                String responseStr =  EntityUtils.toString(response.getEntity());
                return Utils.fromJson(responseStr,new TypeToken<LogisticsResponseDTO>(){});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void updateShoppingCartStatus(ProductVo productVo){
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setStatus(2);
        shoppingCart.setId(productVo.getScId());
        shoppingCartMapper.updateByPrimaryKeySelective(shoppingCart);
    }

    private void updateProductSellNum(ProductVo productVo){
        Product product = new Product();
        product.setId(productVo.getProductId());
        product.setSellNum(product.getSellNum()+productVo.getNum());
        productMapper.updateByPrimaryKeySelective(product);
    }

    private void updateCCSellNum(ProductVo productVo){
        ClothingConfig clothingConfig = new ClothingConfig();
        clothingConfig.setId(productVo.getCcId());
        clothingConfig.setSellNum(clothingConfig.getSellNum()+productVo.getNum());
        clothingConfigMapper.updateByPrimaryKeySelective(clothingConfig);
    }
}
