package com.oreilly.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

@Configuration
@EnableWebSecurity
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ADMIN")
//                .and()
//                .withUser("user").password("user").roles("USER");
//    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user1").password("user1")
                .authorities("ROLE_USER")
                .and()
                .withUser("admin").password("admin")
                .authorities("ROLE_USER", "ROLE_FOO")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //BASIC AUTH
//        http.authorizeRequests()
//                .anyRequest()
//                .hasRole("USER")
//                .and()
//                .httpBasic();
//        http.authorizeRequests()
//                .antMatchers("/appointments/*").hasRole("USER")
//                .antMatchers("/schedule/*").hasRole("FOO")
////                .antMatchers("/*").hasRole("ANONYMOUS")
//                    .and().httpBasic()
//                .and()
//             .authorizeRequests()
//                .antMatchers("/*").hasRole("ANONYMOUS")
//        ;

        //FORM LOGIN
        http.authorizeRequests()
                .antMatchers("/appointments/*").hasRole("USER")
                .antMatchers("/schedule/*").hasRole("FOO")
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login")
                .usernameParameter("custom_username").passwordParameter("custom_password")

                .and()
                .authorizeRequests()
                .antMatchers("/*").hasRole("ANONYMOUS")
        ;
    }
}

//extends AbstractSecurityWebApplicationInitializer