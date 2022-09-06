package tech.outspace.papershare.config.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import tech.outspace.papershare.filter.JwtVerifyFilter;
import tech.outspace.papershare.service.auth.handler.JwtFailedHandler;

public class JwtVerifyConfig<T extends JwtVerifyConfig<T, B>, B extends HttpSecurityBuilder<B>> extends AbstractHttpConfigurer<T, B> {
    private final JwtVerifyFilter jwtVerifyFilter;

    public JwtVerifyConfig() {
        this.jwtVerifyFilter = new JwtVerifyFilter();
    }

    @Override
    public void configure(B builder) {
        jwtVerifyFilter.setAuthenticationManager(builder.getSharedObject(AuthenticationManager.class));
        jwtVerifyFilter.setFailureHandler(new JwtFailedHandler());
        JwtVerifyFilter filter = postProcess(jwtVerifyFilter);
        builder.addFilterBefore(filter, LogoutFilter.class);
    }

    public JwtVerifyConfig<T, B> permissiveRequestUrls(String... urls) {
        jwtVerifyFilter.setPermissiveUrl(urls);
        return this;
    }

    public JwtVerifyConfig<T, B> tokenValidSuccessHandler(AuthenticationSuccessHandler successHandler) {
        jwtVerifyFilter.setSuccessHandler(successHandler);
        return this;
    }
}
