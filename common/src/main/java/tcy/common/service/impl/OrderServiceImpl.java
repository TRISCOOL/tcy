package tcy.common.service.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcy.common.exception.ResponseCode;
import tcy.common.exception.TcyException;
import tcy.common.mapper.*;
import tcy.common.model.*;
import tcy.common.model.wx.PayInfo;
import tcy.common.model.wx.PayResponseInfo;
import tcy.common.service.OrderService;
import tcy.common.utils.DateTimeUtil;
import tcy.common.utils.Utils;
import tcy.common.utils.WxPayUtils;

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
    private ClothingConfigMapper clothingConfigMapper;

    private static String WXXDURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

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
        PayInfo payInfo = PayInfo.getPayInfo(order);
        String nonceStr = WxPayUtils.getNonceStr();
        payInfo.setNonceStr(nonceStr);
        String sign = WxPayUtils.getPaySign(payInfo);
        payInfo.setSign(sign);
        payInfo.setSpbillCreateIp(spBillCreateIp);

        Document payXml = getXmlByPayInfo(payInfo);
        String xml = payXml.asXML();

        PayResponseInfo responseInfo = connectWxPay(xml,WXXDURL);

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
                String content = EntityUtils.toString(response.getEntity());
                PayResponseInfo responseInfo = analysisPaResponseXMl(content);
                if (responseInfo == null)
                    throw new TcyException(ResponseCode.SERVER_ERROR);

                if (responseInfo.getResultCode() != null && responseInfo.getReturnCode() != null &&
                        responseInfo.getReturnCode().equals("SUCCESS") && responseInfo.getResultCode().equals("SUCCESS")){
                    return responseInfo;
                }else {
                    logger.error("wx pay response error return code is {},result code is {}",
                            responseInfo.getReturnCode(),
                            responseInfo.getResultCode());
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
            Document document = DocumentHelper.parseText(xml);
            PayResponseInfo payResponseInfo = PayResponseInfo.getPayResponseInfo(document);
            return payResponseInfo;
        } catch (DocumentException e) {
            logger.error("analysis xml failed with {}",xml);
            throw new TcyException(e);
        }
    }

    private Document getXmlByPayInfo(PayInfo payInfo){
        Document document = DocumentHelper.createDocument();

        document.addElement("appid").setText(payInfo.getAppId());
        document.addElement("body").setText(payInfo.getBody());
        document.addElement("mch_id").setText(payInfo.getMchId());
        document.addElement("nonce_str").setText(payInfo.getNonceStr());
        document.addElement("notify_url").setText(payInfo.getNotifyUrl());
        document.addElement("out_trade_no").setText(payInfo.getOutTradeNo());
        document.addElement("spbill_create_ip").setText(payInfo.getSpbillCreateIp());
        document.addElement("total_fee").setText(payInfo.getTotalFee());
        document.addElement("trade_type").setText(payInfo.getTradeType());
        document.addElement("sign").setText(payInfo.getSign());

        return document;
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
            }
            return true;
        }

        return false;
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