package tcy.common.exception;

public enum ResponseCode {

    OK("200","success"),
    //300-399 订单错误
    NOT_FOUND_LOGISTICS_DETAILS("301","not found logistics traces"),
    //400-500
    AUTH_FAILED("401","User authentication failed"),
    PARAM_ILLEGAL("420","param is illegal"),
    NOT_FOUND_USER("421","not found this user"),
    PASSWORD_ERROR("422","password is error"),
    //500-600
    SERVER_ERROR("500","server error"),
    //600-700
    FILE_UPLOAD_ERROR("601","file upload failed"),
    //1000-1100 product error
    PRODUCT_SHELF("1001","product have was shelfed"),
    PRODUCT_NUM_NEED_MATCH("1002","product stock is must equals every config num"),
    //1100-1200
    WX_CONNECT_ERROR("1101","connect wx pay service failed"),
    WX_HTTP_ERROR("1102","http response error"),
    WX_RESPONSE_ERROR("1103","wx response error");

    private String code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    ResponseCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
