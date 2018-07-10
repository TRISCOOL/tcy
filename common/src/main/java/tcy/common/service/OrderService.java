package tcy.common.service;

import tcy.common.model.Order;
import tcy.common.model.OrderVo;
import tcy.common.model.ShoppingCart;
import tcy.common.model.wx.PayResponseInfo;

import java.util.List;

public interface OrderService {

    Order createOrder(Long addressId, List<ShoppingCart> shoppingCarts,Long userId);

    Order selectOrderById(Long orderId);

    Order selectOrderForCallBack(String orderNumber);

    OrderVo getOrderDetails(Long orderId);

    PayResponseInfo confirmPay(Long orderId,String spBillCreateIp);

    /**
     *
     * @param userId
     * @param type 0-待支付，1-待收货，2-以完成，3-以取消
     * @return
     */
    List<Order> listOrder(Long userId,Integer type);

    boolean updateOrderWithPay(Order order);

    boolean updateByPrimaryKeySelective(Order order);

}
