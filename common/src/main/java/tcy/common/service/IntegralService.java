package tcy.common.service;

import tcy.common.model.IntegralRecord;
import tcy.common.model.Order;
import tcy.common.model.ShareOperationRecord;

public interface IntegralService {

    /**
     * 通过完成的订单更新用户积分
     */
    void updateIntegralForUserByOrder(Order order);

    /**
     * 根据现金兑换，更新积分
     * @param integralRecord
     */
    void updateIntegralForUserByMoney(IntegralRecord integralRecord);

    /**
     * 根据分享完成购买，更新积分
     * @param shareOperationRecord
     */
    void updateIntegralForUserByShare(ShareOperationRecord shareOperationRecord);
}
