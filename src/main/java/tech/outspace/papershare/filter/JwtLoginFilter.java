package tech.outspace.papershare.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.core.log.LogMessage;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.StreamUtils;
import tech.outspace.papershare.model.vo.LoginVo;
import tech.outspace.papershare.utils.convert.JsonConvert;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {

    public JwtLoginFilter() {
        super(new AntPathRequestMatcher("/auth/login", "POST"));
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        Assert.notNull(getSuccessHandler(), "successHandler must be specified");
        Assert.notNull(getFailureHandler(), "failureHandler must be specified");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        String cont = null;
        try {
            cont = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new AuthenticationCredentialsNotFoundException("Null authentication request");
        }

        LoginVo signInVo = getSignInVo(cont);

        UsernamePasswordAuthenticationToken token = null;
        if (signInVo == null) {
            token = new UsernamePasswordAuthenticationToken("", "");
        } else {
            token = new UsernamePasswordAuthenticationToken(
                    signInVo.getEmail(), signInVo.getPass());
        }

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);

        SecurityContextHolder.setContext(context);
        if (this.logger.isDebugEnabled()) {
            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
        }
        this.getRememberMeServices().loginSuccess(request, response, authResult);
        if (this.eventPublisher != null) {
            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
        }
        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
        chain.doFilter(request, response);
    }

    private LoginVo getSignInVo(String cont) {
        if (cont == null) {
            return null;
        }
        LoginVo signInVo = null;
        try {
            signInVo = JsonConvert.toObject(cont, LoginVo.class);
        } catch (JsonProcessingException ignored) {
            throw new BadCredentialsException("Illegal json");
        }
        return signInVo;
    }
}
