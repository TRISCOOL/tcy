package tcy.common.exception;

public enum ResponseCode {

    OK("200","success"),
    //400-500
    AUTH_FAILED("401","User authentication failed"),
    PARAM_ILLEGAL("420","param is illegal"),
    //500-600
    SERVER_ERROR("500","server error");

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
