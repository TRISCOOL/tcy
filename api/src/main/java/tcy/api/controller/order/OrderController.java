package tcy.api.controller.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcy.api.controller.BaseController;
import tcy.api.vo.ResponseVo;
import tcy.common.exception.ResponseCode;
import tcy.common.mapper.IntegralConfigMapper;
import tcy.common.mapper.ShareOperationRecordMapper;
import tcy.common.mapper.ShoppingCartMapper;
import tcy.common.model.*;
import tcy.common.model.wx.PayResponseInfo;
import tcy.common.model.wx.WxNotify;
import tcy.common.service.IntegralService;
import tcy.common.service.OrderService;
import tcy.common.service.RedisService;
import tcy.common.service.ShareService;
import tcy.common.utils.DateTimeUtil;
import tcy.common.utils.Utils;
import tcy.common.utils.WxPayUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseController{


    private Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private ShareService shareService;

    @Autowired
    private IntegralService integralService;

    private static final String CALL_BACK_KEY = "callback";

    /**
     * 创建订单
     * @param shoppingCarts
     * @return
     */
    @PostMapping(value = "/create/v1.1",consumes = "application/json; charset=utf-8")
    public ResponseVo createOrder(@RequestBody List<ShoppingCart> shoppingCarts,
                                  @RequestParam("addressId")Long addressId,
                                  HttpServletRequest request){

        User user = curUser(request);

        if (shoppingCarts == null || addressId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        if (shoppingCarts.size() <= 0){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        Order order = orderService.createOrder(addressId,shoppingCarts,user.getId());

        Map<String,Object> result = new HashMap<String, Object>();
        result.put("orderId",order.getId());
        result.put("orderNumb",order.getOrderNumber());
        result.put("shouldPay",order.getShouldPayAmount());

        return ResponseVo.ok(result);
    }

    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/details/v1.1")
    public ResponseVo orderDetails(@RequestParam("orderId")Long orderId){
        if (orderId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        return ResponseVo.ok(orderService.getOrderDetails(orderId));
    }

    /**
     * 确认支付接口
     * @param orderId
     * @return
     */
    @PostMapping("/pay/v1.1")
    public ResponseVo confirmPay(@RequestParam("orderId")Long orderId,
                                 @RequestParam("customerIp")String customerIp){

        PayResponseInfo responseInfo = orderService.confirmPay(orderId,customerIp);

        String paySign = WxPayUtils.getPaySignToo(responseInfo);

        Map<String,String> result = new HashMap<String, String>();
        result.put("paySign",paySign);

        return ResponseVo.ok(result);
    }

    /**
     * 微信回调通知
     * @return
     */
    @PostMapping(value = "/wx_callback/v1.1",headers = "content-type=application/xml")
    public String wxPayCallBack(@RequestBody WxNotify notify){

        logger.info("wx pay callback ---------- {}",Utils.toJson(notify));

        String returnCode = notify.getReturn_code();
        String returnMsg = notify.getReturn_msg();
        if (returnCode != null && returnCode.equals("SUCCESS")){
            Order order = orderService.selectOrderForCallBack(notify.getOut_trade_no());
            if (order != null){
                if (order.getStatus().equals(2)){
                    return wxPayResponseSuccess();
                }

                if (isContinue(notify.getOut_trade_no())){
                    Order order1 = orderService.selectOrderForCallBack(notify.getOut_trade_no());
                    if (order1.getStatus().equals(2))
                        return wxPayResponseSuccess();

                    if (order1.getStatus().equals(0)){
                        boolean result = updateOrder(order1,notify);
                        if (result){
                            return wxPayResponseSuccess();
                        }
                    }
                }
            }
        }else {
            logger.error("wx pay callback failed code is {},{}",returnCode,returnMsg);
            return "";
        }

        return "";
    }

    public boolean isContinue(String orderNo){
        String key = CALL_BACK_KEY+orderNo;
        Long result = redisService.incrby(key,1);
        if (result == 1L){
            redisService.setExpire(key,2L);
            return true;
        }

        return false;
    }

    private boolean updateOrder(Order order,WxNotify notify){

        Double shouldPay = order.getShouldPayAmount().doubleValue();
        Double totalFee = Double.parseDouble(notify.getTotal_fee());
        if (shouldPay != null && totalFee != null && shouldPay.equals(totalFee)){
            order.setActuallyPaid(new BigDecimal(notify.getCash_fee()));
            order.setStatus(2);
            try {
                order.setPayTime(DateTimeUtil.parseDateTime(notify.getTime_end()));
            } catch (ParseException e) {
                order.setPayTime(new Date());
                logger.error("actually pay time transformation failed {}",notify.getTime_end());
            }
            order.setTareOrderNum(notify.getTransaction_id());
            order.setPayType(0);

            boolean result = orderService.updateOrderWithPay(order);
            if (result){
                return true;
            }
        }
        return false;
    }

    private String wxPayResponseSuccess(){
        return "<xml>\n" +
                "  <return_code>SUCCESS></return_code>\n" +
                "  <return_msg>OK></return_msg>\n" +
                "</xml>";
    }

    /**
     *
     * @param type 0-待支付，1-待收货，2-已完成，3-已取消，4-所有订单
     * @return
     */
    @GetMapping("/list/v1.1")
    public ResponseVo listOrder(@RequestParam("type")Integer type,HttpServletRequest request){

        if (type == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        User user = curUser(request);

        List<Order> orderList = orderService.listOrder(user.getId(),type);

        return ResponseVo.ok(orderList);
    }

    /**
     * 取消订单
     * @return
     */
    @PostMapping("/cancel/v1.1")
    public ResponseVo cancel(@RequestParam("orderId")Long orderId){

        if (orderId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        Order order = orderService.selectOrderById(orderId);
        if (order == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        order.setStatus(5);

        boolean result = orderService.updateByPrimaryKeySelective(order);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }

    /**
     * 删除订单
     * @return
     */
    @PostMapping("/update/v1.1")
    public ResponseVo deleted(@RequestParam("orderId")Long orderId){

        if (orderId == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }

        Order order = orderService.selectOrderById(orderId);
        if (order == null){
            return ResponseVo.error(ResponseCode.PARAM_ILLEGAL);
        }
        order.setDelete(1);

        boolean result = orderService.updateByPrimaryKeySelective(order);
        if (result){
            return ResponseVo.ok();
        }

        return ResponseVo.error(ResponseCode.SERVER_ERROR);
    }
}
