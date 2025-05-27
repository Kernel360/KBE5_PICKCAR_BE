package exception;


public record ErrorResponse(
        ErrorCode errorCode,
        String reason
) {
}
