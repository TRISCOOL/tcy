package tcy.common.model.wx;

import org.dom4j.Document;
import org.dom4j.Element;

public class PayResponseInfo {
    private String returnCode;
    private String returnMsg;
    private String appId;
    private String mchId;
    private String nonceStr;
    private String openId;
    private String sign;
    private String resultCode;
    private String prepayId;
    private String tradeType;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public static PayResponseInfo getPayResponseInfo(Document document){
        if (document == null){
            return null;
        }

        Element rootNode = document.getRootElement();
        PayResponseInfo responseInfo = new PayResponseInfo();
        if (rootNode.element("return_code") != null){
            responseInfo.setReturnCode(rootNode.element("return_code").getText());
        }
        if (rootNode.element("return_msg") != null){
            responseInfo.setReturnMsg(rootNode.element("return_msg").getText());
        }
        if (rootNode.element("appid") != null){
            responseInfo.setAppId(rootNode.element("appid").getText());
        }
        if (rootNode.element("mch_id") != null){
            responseInfo.setMchId(rootNode.element("mch_id").getText());
        }
        if (rootNode.element("nonce_str") != null){
            responseInfo.setNonceStr(rootNode.element("nonce_str").getText());
        }
        if (rootNode.element("openid") != null){
            responseInfo.setOpenId(rootNode.element("openid").getText());
        }
        if (rootNode.element("sign") != null){
            responseInfo.setSign(rootNode.element("sign").getText());
        }
        if (rootNode.element("result_code") != null){
            responseInfo.setResultCode(rootNode.element("result_code").getText());
        }
        if (rootNode.element("prepay_id") != null){
            responseInfo.setPrepayId(rootNode.element("prepay_id").getText());
        }
        if (rootNode.element("trade_type") != null){
            responseInfo.setTradeType(rootNode.element("trade_type").getText());
        }

        return responseInfo;
    }
}
