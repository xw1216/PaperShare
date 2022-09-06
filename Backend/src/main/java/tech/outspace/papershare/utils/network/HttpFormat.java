package tech.outspace.papershare.utils.network;

import org.springframework.http.MediaType;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HttpFormat {
    public final static String authHeader = "Authorization";
    public final static String authPrefix = "Bearer ";
    public final static String originHeader = "Access-Control-Allow-Origin";
    public final static String exposeHeader = "Access-Control-Expose-Headers";
    public final static String allowMethodsHeader = "Access-Control-Allow-Methods";
    public final static String allowHeader = "Access-Control-Allow-Headers";
    public final static String requestHeader = "Access-Control-Request-Headers";
    public final static List<String> methodsList = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD");
    public final static Integer requestMethodsCnt = 4;


    public static void reviseResponse(HttpServletResponse response, int status) {
        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static <T> Result<T> reviseErrorResponse(HttpServletResponse response, EResult result) {
        int status = result.getCode();
        reviseResponse(response, status);
        return Result.factory(result, null);
    }

    public static <T> Result<T> reviseErrorResponse(HttpServletResponse response, EResult result, String msg) {
        int status = result.getCode();
        reviseResponse(response, status);
        return Result.factory(result, msg, null);
    }

    public static void setTokenHeader(HttpServletResponse response, String token) {
        response.setHeader(authHeader, authPrefix + token);
    }
}
