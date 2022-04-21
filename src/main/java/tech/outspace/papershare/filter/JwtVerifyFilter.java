package tech.outspace.papershare.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.outspace.papershare.service.auth.token.JwtVerifyToken;
import tech.outspace.papershare.utils.network.HttpFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JwtVerifyFilter extends OncePerRequestFilter {

    private final RequestMatcher verifyRequestMatcher;
    private List<RequestMatcher> permissiveRequestMatchers;
    private AuthenticationManager authenticationManager;

    private AuthenticationSuccessHandler successHandler;
    private AuthenticationFailureHandler failureHandler;

    public JwtVerifyFilter() {
        this.verifyRequestMatcher = new RequestHeaderRequestMatcher(HttpFormat.authHeader);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        Assert.notNull(getAuthenticationManager(), "authenticationManager must be specified");
        Assert.notNull(getSuccessHandler(), "successHandler must be specified");
        Assert.notNull(getFailureHandler(), "failureHandler must be specified");
    }

    private String parseJwtToken(HttpServletRequest request) {
        String info = request.getHeader(HttpFormat.authHeader);
        if (info == null || !info.startsWith(HttpFormat.authPrefix)) {
            return null;
        }
        return info.split(" ")[1];
    }

    private boolean isRequireVerify(HttpServletRequest request) {
        return verifyRequestMatcher.matches(request);
    }

    private boolean permissiveRequest(HttpServletRequest request) {
        if (permissiveRequestMatchers == null) {
            return false;
        }
        for (RequestMatcher permissiveMatcher : permissiveRequestMatchers) {
            if (permissiveMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public void setPermissiveUrl(String... urls) {
        if (permissiveRequestMatchers == null) {
            permissiveRequestMatchers = new ArrayList<>();
        }
        for (String url : urls) {
            permissiveRequestMatchers.add(new AntPathRequestMatcher(url));
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!isRequireVerify(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication verifyResult = null;
        AuthenticationException verifyException = null;

        String token = parseJwtToken(request);
        try {
            if (token == null || token.trim().isEmpty()) {
                verifyException = new InsufficientAuthenticationException("Jwt Empty");
            } else {
                DecodedJWT jwt = JWT.decode(token);
                JwtVerifyToken jwtVerifyToken = new JwtVerifyToken(jwt);
                verifyResult = this.authenticationManager.authenticate(jwtVerifyToken);
            }
        } catch (JWTDecodeException ex) {
            log.error("Jwt format error", ex);
            verifyException = new InsufficientAuthenticationException("Jwt format error");
        } catch (InternalAuthenticationServiceException ex) {
            log.error("An internal error occurred while trying to verify the account", ex);
            verifyException = ex;
        } catch (AuthenticationException ex) {
            verifyException = ex;
        }

        if (verifyResult != null) {
            successVerify(request, response, filterChain, verifyResult);
        } else if (permissiveRequest(request)) {
            failureVerify(request, response, verifyException);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void successVerify(HttpServletRequest request, HttpServletResponse response,
                               FilterChain chain, Authentication authResult) throws ServletException, IOException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        successHandler.onAuthenticationSuccess(request, response, authResult);
        chain.doFilter(request, response);
    }

    private void failureVerify(HttpServletRequest request, HttpServletResponse response,
                               AuthenticationException failed) throws ServletException, IOException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }

    public AuthenticationSuccessHandler getSuccessHandler() {
        return successHandler;
    }

    public void setSuccessHandler(AuthenticationSuccessHandler successHandler) {
        this.successHandler = successHandler;
    }

    public AuthenticationFailureHandler getFailureHandler() {
        return failureHandler;
    }

    public void setFailureHandler(AuthenticationFailureHandler failureHandler) {
        this.failureHandler = failureHandler;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
