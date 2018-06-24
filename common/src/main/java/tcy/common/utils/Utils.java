package tcy.common.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class Utils {

    public static Gson gson = new Gson();

    private static final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();

    private static String defaultSalt = "vms-cloud";

    private static final String idCardReg18 = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    private static final String idCardReg15 = "^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}$";

    private static final String telReg = "^1([3578][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";

    private static final String vinReg = "^[0-9a-zA-Z]{17}$";

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static <T> T fromJson(String json, TypeToken<T> typeToken){
        if (json == null || "".equals(json))return null;
        return gson.fromJson(json,typeToken.getType());
    }

    public static String toJson(Object o){
        return gson.toJson(o);
    }

    public static String generateToken() {
        return generateToken(null);
    }

    public static String generateToken(String salt) {
        if (StringUtils.isEmpty(salt)) {
            return encryptMD5(getUuid(true), defaultSalt);
        } else {
            return encryptMD5(getUuid(true), salt);
        }
    }

    public static String getUuid(boolean shortFlag) {
        if (shortFlag) {
            return UUID.randomUUID().toString().replace("-", "");
        } else {
            return UUID.randomUUID().toString();
        }
    }

    public static String encryptMD5(String... str) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length; i++) {
                sb.append(str);
            }
            md.update(sb.toString().getBytes());
            return byte2hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static String getMd5(String password){
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try{
            byte[] strTemp = password.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[]=new char[j*2];
            int k=0;
            for(int i=0;i<j;i++){
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(Exception e){
            return null;
        }
    }

    public static boolean idCardMatch(String idCard){
        if(idCard == null) return false;
        return idCard.matches(idCardReg18) || idCard.matches(idCardReg15);
    }

    public static boolean telMatch(String tel){
        if(tel == null) return false;
        return tel.matches(telReg);
    }

    public static boolean vinMatch(String vin){
        if(vin == null) return false;
        return vin.matches(vinReg);
    }
}
