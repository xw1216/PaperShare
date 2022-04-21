package tech.outspace.papershare.utils.result;

import static javax.servlet.http.HttpServletResponse.*;

public enum EResult {
    SUCCESS(SC_OK, "request success"),
    BAD_REQUEST(SC_BAD_REQUEST, "bad request"),
    AUTH_FAIL(SC_UNAUTHORIZED, "authentication failed"),
    DATA_DUPLICATE(SC_PAYMENT_REQUIRED, "duplicate data detected"),
    REQUEST_REJECT(SC_FORBIDDEN, "reject request"),
    DATA_NULL(SC_NOT_FOUND, "empty data"),
    UNKNOWN_ERROR(SC_INTERNAL_SERVER_ERROR, "unknown error");

    private final Integer code;
    private final String msg;

    EResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
