package com.bank.statement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("user")).roles("USER")
                .and().withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN","USER");
    }
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

@Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests().antMatchers("/swagger-resources/*","*.html","/api/v1/swagger.json")
                .hasRole("SWAGGER")
                .anyRequest()
                .authenticated().and().formLogin().and().sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
}
@Bean
    public SessionRegistry sessionRegistry()
{
    SessionRegistry sessionRegistry=new SessionRegistryImpl();
    return sessionRegistry;
}
@Bean
    public HttpSessionEventPublisher httpSessionEventPublisher()
{
    return new HttpSessionEventPublisher();
}
}
