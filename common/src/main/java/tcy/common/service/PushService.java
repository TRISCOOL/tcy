package tcy.common.service;

public interface PushService {

    boolean pushVerificationCode(Long userId,String code);

    boolean pushOrderSuccess(Long userId,String orderNumber);

    boolean pushShip(Long userId,String orderNumber);

    boolean pushTradeFinished(Long userId,String orderNumber);
}
