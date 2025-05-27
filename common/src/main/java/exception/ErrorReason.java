package exception;

public class ErrorReason {
    private Integer status;
    private String code;
    private String reason;

    public ErrorReason(Integer status, String code, String reason) {
        this.status = status;
        this.code = code;
        this.reason = reason;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getReason() {
        return reason;
    }
}
