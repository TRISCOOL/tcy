package tcy.api.vo;

/**
 * @RequestParam("wxCode")String wxCode,
 @RequestParam("encryptedData")String encryptedData,
 @RequestParam("iv") String iv
 */
public class WxRequest {
    private String wxCode;
    private String encryptedData;
    private String iv;
    private String wxNickName;
    private String wxGender;
    private String url;

    public String getWxNickName() {
        return wxNickName;
    }

    public void setWxNickName(String wxNickName) {
        this.wxNickName = wxNickName;
    }

    public String getWxGender() {
        return wxGender;
    }

    public void setWxGender(String wxGender) {
        this.wxGender = wxGender;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }
}
