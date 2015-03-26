package com.rdas.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
* Created by rdas on 25/03/2015.
*
*
*/
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final Logger log = Logger.getLogger(SecurityConfig.class);

    /**
     * The name of the configureGlobal method is not important.
     * However, it is important to only configure AuthenticationManagerBuilder in a class annotated with either :
     * @EnableWebSecurity, @EnableWebMvcSecurity, @EnableGlobalMethodSecurity, or @EnableGlobalAuthentication
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        log.debug("\n\n\n --> configureGlobal \n\n");
        auth.
                inMemoryAuthentication().
                withUser("user").password("password").roles("USER").
                and().
                withUser("rana").password("rdas").roles("USER", "ADMIN").
                and().
                withUser("jennifer").password("jen").roles("USER");
        auth.inMemoryAuthentication().withUser("james").password("123456").roles("SUPERADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("\n\n\n --> configure \n\n");
        http.
//                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().
                authorizeRequests().
                antMatchers("/protected/**").access("hasRole('ROLE_ADMIN')").
                antMatchers("/confidential/**").access("hasRole('ROLE_SUPERADMIN')").
                and().formLogin();

//        super.configure(http);
//        http.
//                csrf().disable().
//                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).
//                and().
//                authorizeRequests().
////                antMatchers("/protected**").hasAuthority("ADMIN").
////                antMatchers("/confidential**").hasAuthority("ADMIN").
//                and().
//                httpBasic().realmName("x");
    }

}
