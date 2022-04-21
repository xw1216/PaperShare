package tech.outspace.papershare.service.auth.provider;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.model.entity.objs.Session;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.service.auth.AuthProviderService;
import tech.outspace.papershare.service.auth.token.JwtVerifyToken;
import tech.outspace.papershare.utils.encrypt.JwtUtil;
import tech.outspace.papershare.utils.time.TimeUtil;

@Service
public class JwtVerifyProvider implements AuthenticationProvider {
    private final AuthProviderService authProviderService;

    public JwtVerifyProvider(AuthProviderService authProviderService) {
        this.authProviderService = authProviderService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        DecodedJWT decodedJWT = ((JwtVerifyToken) authentication).getToken();
        TokenVo tokenVo = parseTokenVo(decodedJWT);

        UserDetails userDetails = authProviderService.loadUserById(tokenVo.getUserId());
        checkAccountLock(userDetails);

        Session session = authProviderService.loadSessionById(tokenVo.getSessionId());
        checkTokenEncrypt(decodedJWT.getToken(), tokenVo, session.getSecret());

        checkTokenExpireTime(decodedJWT, session);
        checkSessionExpire(session);

        return new JwtVerifyToken(userDetails, decodedJWT, userDetails.getAuthorities());
    }

    private void checkAccountLock(UserDetails userDetails) {
        if (!(userDetails.isAccountNonLocked())) {
            throw new LockedException("User Account Baned");
        }
    }

    private void checkSessionExpire(Session session) {
        if (session.getClose()) {
            authProviderService.deleteSessionById(session.getId());
            throw new NonceExpiredException("Token Session Closed");
        }

        if (TimeUtil.isSessionExpired(session)) {
            authProviderService.deleteSessionById(session.getId());
            throw new NonceExpiredException("Token Session Expires");
        }

        authProviderService.delaySessionExpireTimeById(
                session.getId(), TimeUtil.getSessionExpireTime());
    }

    private void checkTokenExpireTime(DecodedJWT decodedJWT, Session session) {
        if (TimeUtil.isJwtExpired(decodedJWT)) {
            authProviderService.deleteSessionById(session.getId());
            throw new NonceExpiredException("Token Expires");
        }
    }

    private void checkTokenEncrypt(String jwt, TokenVo tokenVo, String secret) {
        if (!(JwtUtil.verify(tokenVo, secret, jwt))) {
            throw new BadCredentialsException("Token falsify");
        }
    }

    private TokenVo parseTokenVo(DecodedJWT decodedJWT) {

        TokenVo tokenVo = JwtUtil.decodePayload(decodedJWT);
        if (tokenVo == null || tokenVo.getUserId() == null || tokenVo.getSessionId() == null) {
            throw new InsufficientAuthenticationException("Jwt format error");
        }
        return tokenVo;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JwtVerifyToken.class);
    }

}
