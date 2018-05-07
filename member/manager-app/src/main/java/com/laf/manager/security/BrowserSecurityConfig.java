package com.laf.manager.security;

import com.laf.manager.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AuthenticationSuccessHandler managerAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler managerAuthenticationFailureHandler;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);

        return tokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

//        ValidateCodeFilter validateCodeFilter = new ValidateCodeFilter();
//        validateCodeFilter.setAuthenticationFailureHandler(managerAuthenticationFailureHandler);
//        validateCodeFilter.setSecurityProperties(securityProperties);
//        validateCodeFilter.afterPropertiesSet();

        http
//            .addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class) 图形验证码过滤器
            .formLogin()
            .loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form") // 貌似只能拦截get请求
            .successHandler(managerAuthenticationSuccessHandler)
            .failureHandler(managerAuthenticationFailureHandler)
            .and()
            .rememberMe()
            .tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
            .userDetailsService(userDetailsService)
            .and()
            .authorizeRequests() // 请求授权
            .antMatchers("/authentication/require",
                    securityProperties.getBrowser().getLoginPage()).permitAll()
            .antMatchers("/js/**").permitAll()
            .antMatchers("/css/**").permitAll()
            .antMatchers("/images/**").permitAll()
            .antMatchers("/vendors/**").permitAll()
            .antMatchers("/fonts/**").permitAll()
            .antMatchers("/upload/**").permitAll()
            .anyRequest() // 任何请求
            .authenticated() // 身份认证
            .and()
            .csrf().disable();
    }

}
