package tcy.common.utils;

import tcy.common.model.wx.PayInfo;
import tcy.common.model.wx.PayResponseInfo;

import java.util.Date;

public class WxPayUtils {

    private static String PAYKEY = "";

    public static String getPaySign(PayInfo payInfo){

        String stringA = "appid="+payInfo.getAppId()+"&body="+payInfo.getBody()+"&mch_id="+payInfo.getMchId()+"&="
                +"nonce_str="+payInfo.getNonceStr()+"&notify_url="+payInfo.getNotifyUrl()+"&out_trade_no="
                +payInfo.getOutTradeNo()+"&spbill_create_ip=" +payInfo.getSpbillCreateIp()+"&total_fee="
                +payInfo.getTotalFee()+"&trade_type="+payInfo.getTradeType();

        String stringSignTemp = stringA+"&key="+PAYKEY;
        String sign = Utils.getMd5(stringSignTemp);
        return sign;
    }

    public static String getPaySignToo(PayResponseInfo responseInfo){
        if (responseInfo == null){
            return null;
        }

        String content = "appId="+responseInfo.getAppId()+"&nonceStr="+responseInfo.getNonceStr()
                +"&package=prepay_id="+responseInfo.getPrepayId()+"&signType=MD5&timeStamp="+new Date().getTime()/1000
                +"&key="+PAYKEY;

        return Utils.getMd5(content);
    }

    public static String getNonceStr(){
        return Utils.getUuid(true).substring(0,16);
    }


}
