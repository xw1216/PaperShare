package tech.outspace.papershare.service.auth.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.service.auth.AuthProviderService;
import tech.outspace.papershare.service.auth.token.JwtVerifyToken;
import tech.outspace.papershare.utils.convert.JsonConvert;
import tech.outspace.papershare.utils.network.HttpFormat;
import tech.outspace.papershare.utils.result.EResult;
import tech.outspace.papershare.utils.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SessionLogoutHandler implements LogoutHandler {
    private final AuthProviderService authProviderService;

    public SessionLogoutHandler(AuthProviderService authProviderService) {
        this.authProviderService = authProviderService;
    }


    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication == null) {
            return;
        }

        TokenVo tokenVo = ((JwtVerifyToken) authentication).getTokenVo();
        Result<String> result = Result.factory(EResult.SUCCESS, "Logout Success");
        try {
            String json = JsonConvert.toJson(result);
            authProviderService.logoutById(tokenVo.getSessionId());
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            HttpFormat.reviseResponse(response, EResult.UNKNOWN_ERROR.getCode());
        }

    }
}
