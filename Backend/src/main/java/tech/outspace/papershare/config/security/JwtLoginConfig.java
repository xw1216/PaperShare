package tech.outspace.papershare.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import tech.outspace.papershare.filter.JwtLoginFilter;
import tech.outspace.papershare.service.auth.handler.JwtFailedHandler;

public class JwtLoginConfig<T extends JwtLoginConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    private final JwtLoginFilter jwtLoginFilter;

    public JwtLoginConfig() {
        this.jwtLoginFilter = new JwtLoginFilter();
    }

    @Override
    public void configure(B builder) {
        jwtLoginFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        jwtLoginFilter.setAuthenticationFailureHandler(new JwtFailedHandler());
        JwtLoginFilter filter = postProcess(jwtLoginFilter);
        builder.addFilterAfter(filter, LogoutFilter.class);
    }

    public JwtLoginConfig<T, B> loginSuccessHandler(AuthenticationSuccessHandler handler) {
        jwtLoginFilter.setAuthenticationSuccessHandler(handler);
        return this;
    }
}
