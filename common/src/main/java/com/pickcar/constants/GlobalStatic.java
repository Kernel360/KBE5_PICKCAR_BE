package com.pickcar.constants;

public final class GlobalStatic {

    public static final String ERROR_PREFIX = "[ERROR] ";

    public enum HttpStatus {
        //2XX Success
        OK(200, "요청이 정상적으로 처리되었습니다."),
        CREATED(201, "요청을 정상적으로 처리하고, 새로운 리소스가 생성되었습니다."),
        ACCEPTED(202, "요청을 정상적으로 수신하였으나, 처리가 진행되는 중입니다."),      //NOTE: 주기정보에서 활용 가능?(Queue)
        NO_CONTENT(204, "요청을 정상적으로 수행하였고, 반환할 내용은 없습니다."),

        //4XX Client Error
        BAD_REQUEST(400, "클라이언트의 요청이 올바르지 않습니다."),
        UNAUTHORIZED(401, "인증되지 않은 사용자입니다. (Unauthenticate)"),
        FORBIDDEN(403, "접근 권한이 없는 사용자입니다. (Unauthorized)"),
        NOT_FOUND(404, "요청한 내용을 찾을 수 없습니다."),
        CONFLICT(409, "요청을 처리할 수 없습니다."),
        TOO_MANY_REQUESTS(429, "요청이 너무 많습니다."),

        //5XX Server Error
        INTERNAL_SERVER_ERROR(500, "서버 내부에 문제가 발생하였습니다.");

        private final Integer code;
        private final String description;

        HttpStatus(Integer code, String description) {
            this.code = code;
            this.description = description;
        }

        public Integer getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }
    }

    public static final class MDCConstants {
        public static final String API_PREFIX = "/api/v1";
        public static final String TRACE_ID_KEY = "traceId";
        public static final String MODULE_NAME_KEY = "moduleName";
        public static final String SERVICE_NAME_KEY = "service";
        public static final String STATUS_CODE_KEY = "statusCode";
    }
}
