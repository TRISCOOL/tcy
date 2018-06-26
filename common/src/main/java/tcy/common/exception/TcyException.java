package tcy.common.exception;

public class TcyException extends RuntimeException{

    private ResponseCode errorCode;

    public TcyException() {
    }

    public TcyException(String message) {
        super(message);
        ResponseCode errorCode = ResponseCode.SERVER_ERROR;
        this.errorCode = errorCode;
    }

    public TcyException(String message, Throwable cause) {
        super(message, cause);
    }

    public TcyException(Throwable cause) {
        super(cause);
    }

    public TcyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public TcyException(ResponseCode code){
        this.errorCode = code;
    }

    public ResponseCode getErrorCode() {
        return errorCode;
    }
}
