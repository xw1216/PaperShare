package tech.outspace.papershare.service.auth.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import tech.outspace.papershare.model.vo.TokenVo;
import tech.outspace.papershare.utils.encrypt.JwtUtil;

import javax.security.auth.Subject;
import java.util.Collection;
import java.util.Collections;

public class JwtVerifyToken extends AbstractAuthenticationToken {
    private UserDetails principal;
    private String credentials;
    private DecodedJWT token;

    public JwtVerifyToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    public JwtVerifyToken(DecodedJWT token) {
        super(Collections.emptyList());
        this.token = token;
    }

    public JwtVerifyToken(UserDetails principal, DecodedJWT token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
    }

    @Override
    public void setDetails(Object details) {
        super.setDetails(details);
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }

    public DecodedJWT getToken() {
        return token;
    }

    public TokenVo getTokenVo() {
        return JwtUtil.decodePayload(token);
    }
}
