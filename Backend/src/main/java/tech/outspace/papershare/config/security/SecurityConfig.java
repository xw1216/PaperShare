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
        // 向 AuthenticationManager 中添加自定义的 Provider
        // 其验证到支持的 Token 封装类型后就会被调用
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
                                        // 设置无需认证的页面
                                        .mvcMatchers("/index").permitAll()
                                        .mvcMatchers("/auth/register").permitAll()
                                        .mvcMatchers("/auth/login").permitAll()
                                        .mvcMatchers("/api-docs/**", "/api-test/**").permitAll()
                                        .mvcMatchers("/swagger-ui/**").permitAll()
                                        .mvcMatchers("/auth/email/**").permitAll()
                                        // 设置需要认证的页面
//                                        .mvcMatchers("/greeting/**").hasRole("ADMIN")
                                        .anyRequest().authenticated()
                        // 其他控制器页面可以使用注解配置
                )
                // 设置跨域支持
                .cors(Customizer.withDefaults())
                // 关闭 CSRF 保护
                .csrf().disable()
                // 关闭默认 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 关闭浏览器内置 form 登录
                .formLogin().disable()
                // 设置支持跨域与 Jwt 验证的请求头
                .headers().addHeaderWriter(new StaticHeadersWriter(Arrays.asList(
                        new Header(HttpFormat.originHeader, "*"),
                        new Header(HttpFormat.exposeHeader, HttpFormat.authHeader)
                )))
                .and()
                // 设置请求认证
                .apply(new JwtLoginConfig<>())
                .loginSuccessHandler(jwtSuccessHandler)
                .and()
                // 设置 Verify 处理链路
                .apply(new JwtVerifyConfig<>())
                .tokenValidSuccessHandler(jwtSuccessHandler)
                .permissiveRequestUrls("/auth/logout")
                .and()
                // 拦截 OPTIONS 请求
                .apply(new OptionsConfig<>())
                .and()
                // 设置 Logout 默认处理链路
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(sessionLogoutHandler)
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }
}
