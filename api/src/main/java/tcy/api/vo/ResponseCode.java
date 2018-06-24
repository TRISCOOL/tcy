package tcy.api.vo;

public enum ResponseCode {

    OK("200","success");

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
