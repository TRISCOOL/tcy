package tcy.common.model.wx;

import tcy.common.model.Order;
import tcy.common.utils.Constant;

public class PayInfo {
    private String appId; //微信分配的小程序ID
    private String mchId; //微信支付分配的商户号
    private String nonceStr; //随机字符串;
    private String sign; //签名;
    private String body; //商品描述
    private String outTradeNo; //商户订单;
    private Integer totalFee; //订单总金额，单位为分
    private String spbillCreateIp; //APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
    private String notifyUrl; //通知地址
    private String tradeType; //交易类型 -JSAPI
    private String openId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Integer totalFee) {
        this.totalFee = totalFee;
    }

    public String getSpbillCreateIp() {
        return spbillCreateIp;
    }

    public void setSpbillCreateIp(String spbillCreateIp) {
        this.spbillCreateIp = spbillCreateIp;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public static PayInfo getPayInfo(Order order,String customerIp){
        PayInfo payInfo = new PayInfo();
        payInfo.setAppId(Constant.WX_APP_ID);
        payInfo.setMchId(Constant.MCHID);
        payInfo.setBody("服装交易");
        payInfo.setNotifyUrl(Constant.NOTIFYURL);
        payInfo.setTotalFee(order.getShouldPayAmount() != null?order.getShouldPayAmount().intValue()*100:0);
        payInfo.setTradeType(Constant.TRADETYPE);
        payInfo.setOutTradeNo(order.getOrderNumber());
        payInfo.setOpenId(order.getOpenId());
        payInfo.setSpbillCreateIp(customerIp);

        return payInfo;
    }
}
