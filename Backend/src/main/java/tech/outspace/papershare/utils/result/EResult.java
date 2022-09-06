package tech.outspace.papershare.utils.result;

import static javax.servlet.http.HttpServletResponse.*;

public enum EResult {
    SUCCESS(SC_OK, "请求成功"),
    BAD_REQUEST(SC_BAD_REQUEST, "请求错误"),
    AUTH_FAIL(SC_UNAUTHORIZED, "身份验证失败"),
    DATA_DUPLICATE(SC_PAYMENT_REQUIRED, "数据重复"),
    REQUEST_REJECT(SC_FORBIDDEN, "请求驳回"),
    DATA_NULL(SC_NOT_FOUND, "空数据"),
    UNKNOWN_ERROR(SC_INTERNAL_SERVER_ERROR, "未知错误");

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
