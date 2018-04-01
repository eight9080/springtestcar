package com.dg.security.config;

import com.dg.security.sec.CustomAuthenticationFilter;
import com.dg.security.sec.CustomAuthenticationProvider;
import com.dg.security.sec.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

   @Bean
   public CustomAuthenticationFilter authenticationFilter() throws Exception{

       final CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
       filter.setAuthenticationManager(authenticationManagerBean());
       filter.setUsernameParameter("custom_username");
       filter.setPasswordParameter("custom_password");

       filter.setFilterProcessesUrl("/login");

       filter.setAuthenticationSuccessHandler(successHandler());
       filter.setAuthenticationFailureHandler(failureHandler());

       return filter;
   }

    @Bean
    public SavedRequestAwareAuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setDefaultTargetUrl("/appointments/");
        //successHandler.setTargetUrlParameter("/secure/");
        return successHandler;
    }

    @Bean
    public SimpleUrlAuthenticationFailureHandler failureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
        failureHandler.setDefaultFailureUrl("/login/failure?error=true");
        return failureHandler;
    }

    public LoginUrlAuthenticationEntryPoint loginEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/login");
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ADMIN")
//                .and()
//                .withUser("user").password("user").roles("USER");
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("user1").password("user1")
//                .authorities("ROLE_USER")
//                .and()
//                .withUser("admin").password("admin")
//                .authorities("ROLE_USER", "ROLE_FOO")
//        ;
//    }

    //CUSTOM USER SERVICE
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(customUserDetailsService);
        auth.

                authenticationProvider(customAuthenticationProvider)

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
//        http.authorizeRequests()
//                .antMatchers("/appointments/*").hasRole("USER")
//                .antMatchers("/schedule/*").hasRole("FOO")
//                .and()
//                .formLogin().loginPage("/login").loginProcessingUrl("/login")
//                .usernameParameter("custom_username").passwordParameter("custom_password")
//                .defaultSuccessUrl("/appointments/")
//                .defaultSuccessUrl("/appointments/", true)
//                .failureUrl("/login?error=true")
//                .and()
//                .authorizeRequests()
//                .antMatchers("/*").permitAll()
//        //.hasRole("ANONYMOUS")
////        ;

        http.authorizeRequests()
                .antMatchers("/appointments/*").hasRole("USER")
                .antMatchers("/schedule/*").hasRole("FOO")
                .and()
                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers("/*").permitAll()

        //.hasRole("ANONYMOUS")
        ;
        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true");

        http.exceptionHandling().authenticationEntryPoint(loginEntryPoint());

    }
}

//extends AbstractSecurityWebApplicationInitializer