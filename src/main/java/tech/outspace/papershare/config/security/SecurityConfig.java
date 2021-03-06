package tech.outspace.papershare.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.header.Header;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import tech.outspace.papershare.service.auth.handler.JwtSuccessHandler;
import tech.outspace.papershare.service.auth.handler.SessionLogoutHandler;
import tech.outspace.papershare.service.auth.provider.JwtLoginProvider;
import tech.outspace.papershare.service.auth.provider.JwtVerifyProvider;
import tech.outspace.papershare.utils.network.HttpFormat;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtSuccessHandler jwtSuccessHandler;
    private final JwtLoginProvider jwtSignInProvider;
    private final JwtVerifyProvider jwtVerifyProvider;
    private final SessionLogoutHandler sessionLogoutHandler;

    public SecurityConfig(JwtSuccessHandler jwtSuccessHandler, JwtLoginProvider jwtSignInProvider,
                          JwtVerifyProvider jwtVerifyProvider, SessionLogoutHandler sessionLogoutHandler) {
        this.jwtSuccessHandler = jwtSuccessHandler;
        this.jwtSignInProvider = jwtSignInProvider;
        this.jwtVerifyProvider = jwtVerifyProvider;
        this.sessionLogoutHandler = sessionLogoutHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // ??? AuthenticationManager ????????????????????? Provider
        // ????????????????????? Token ??????????????????????????????
        auth
                .authenticationProvider(jwtSignInProvider)
                .authenticationProvider(jwtVerifyProvider)
                .eraseCredentials(false);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) ->
                                authorize
                                        // ???????????????????????????
                                        .mvcMatchers("/index").permitAll()
                                        .mvcMatchers("/auth/register").permitAll()
                                        .mvcMatchers("/auth/login").permitAll()
                                        .mvcMatchers("/api-docs/**", "/api-test/**").permitAll()
                                        .mvcMatchers("/swagger-ui/**").permitAll()
                                        .mvcMatchers("/auth/email/**").permitAll()
                                        // ???????????????????????????
//                                        .mvcMatchers("/greeting/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                        // ?????????????????????????????????????????????
                )
                // ??????????????????
                .cors(Customizer.withDefaults())
                // ?????? CSRF ??????
                .csrf().disable()
                // ???????????? session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // ????????????????????? form ??????
                .formLogin().disable()
                // ????????????????????? Jwt ??????????????????
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                        new Header(HttpFormat.originHeader, "*"),
                        new Header(HttpFormat.exposeHeader, HttpFormat.authHeader)
                )))
                .and()
                // ??????????????????
                .apply(new JwtLoginConfig<>())
                .loginSuccessHandler(jwtSuccessHandler)
                .and()
                // ?????? Verify ????????????
                .apply(new JwtVerifyConfig<>())
                .tokenValidSuccessHandler(jwtSuccessHandler)
                .permissiveRequestUrls("/auth/logout")
                .and()
                // ?????? OPTIONS ??????
                .apply(new OptionsConfig<>())
                .and()
                // ?????? Logout ??????????????????
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(sessionLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
}
