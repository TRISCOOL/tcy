package tcy.common.model.wx;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * <xml>
 <appid><![CDATA[wx2421b1c4370ec43b]]></appid>
 <attach><![CDATA[支付测试]]></attach>
 <bank_type><![CDATA[CFT]]></bank_type>
 <fee_type><![CDATA[CNY]]></fee_type>
 <is_subscribe><![CDATA[Y]]></is_subscribe>
 <mch_id><![CDATA[10000100]]></mch_id>
 <nonce_str><![CDATA[5d2b6c2a8db53831f7eda20af46e531c]]></nonce_str>
 <openid><![CDATA[oUpF8uMEb4qRXf22hE3X68TekukE]]></openid>
 <out_trade_no><![CDATA[1409811653]]></out_trade_no>
 <result_code><![CDATA[SUCCESS]]></result_code>
 <return_code><![CDATA[SUCCESS]]></return_code>
 <sign><![CDATA[B552ED6B279343CB493C5DD0D78AB241]]></sign>
 <sub_mch_id><![CDATA[10000100]]></sub_mch_id>
 <time_end><![CDATA[20140903131540]]></time_end>
 <total_fee>1</total_fee>
 <coupon_fee><![CDATA[10]]></coupon_fee>
 <coupon_count><![CDATA[1]]></coupon_count>
 <coupon_type><![CDATA[CASH]]></coupon_type>
 <coupon_id><![CDATA[10000]]></coupon_id>
 <coupon_fee><![CDATA[100]]></coupon_fee>
 <trade_type><![CDATA[JSAPI]]></trade_type>
 <transaction_id><![CDATA[1004400740201409030005092168]]></transaction_id>
 </xml>
 */
@XmlRootElement(name = "xml")
public class WxNotify {
    private String appid;
    private String return_code;
    private String return_msg;
    private String mch_id;
    private String nonce_str;
    private String sign;
    private String result_code;
    private String openid;
    private String trade_type;
    private String bank_type;
    private String total_fee;
    private String cash_fee;
    private String transaction_id;
    private String out_trade_no;
    private String time_end;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBank_type() {
        return bank_type;
    }

    public void setBank_type(String bank_type) {
        this.bank_type = bank_type;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getCash_fee() {
        return cash_fee;
    }

    public void setCash_fee(String cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }
}
