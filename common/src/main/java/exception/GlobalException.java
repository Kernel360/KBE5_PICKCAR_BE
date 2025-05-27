package exception;

public class GlobalException extends RuntimeException {

    private ErrorCode errorCode;
    private String reason;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getErrorReason().getReason());
        this.errorCode = errorCode;
        this.reason = errorCode.getErrorReason().getReason();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getReason() {
        return reason;
    }
}
