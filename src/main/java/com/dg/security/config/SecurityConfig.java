package com.dg.security.config;

import com.dg.security.sec.CustomAuthenticationFilter;
import com.dg.security.sec.CustomAuthenticationProvider;
import com.dg.security.sec.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig
        extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private DataSource dataSource;

    @Bean
    public FilterRegistrationBean authenticatioRegistrationBean(CustomAuthenticationFilter filter){
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

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

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
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
//        auth.authenticationProvider(customAuthenticationProvider);
        //JDBC datasource
        auth.jdbcAuthentication()
            .dataSource(dataSource)
                .passwordEncoder(passwordEncoder())
            .groupAuthoritiesByUsername("select\n" +
                    "g.id, g.group_name, ga.authority\n" +
                    "from\n" +
                    "groups g, group_members gm, group_authorities ga\n" +
                    "where\n" +
                    "gm.username = ? and g.id = ga.group_id and g.id = gm.group_id")
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
                .antMatchers("/schedule/*").hasAnyRole("FOO")
                .and()
                .formLogin().loginPage("/login").loginProcessingUrl("/login")
                .usernameParameter("custom_username").passwordParameter("custom_password")
                .defaultSuccessUrl("/appointments/")
                .defaultSuccessUrl("/appointments/", true)
                .failureUrl("/login?error=true")
                .and()
                .authorizeRequests()
                .antMatchers("/*").permitAll()
        ;

        //CUSTOM AUTH
//        http.authorizeRequests()
//                .antMatchers("/appointments/*").hasRole("USER")
//                .antMatchers("/schedule/*").hasRole("FOO")
//                .and()
//                .addFilterBefore(authenticationFilter(), UsernamePasswordAuthenticationFilter.class)
//
//                .authorizeRequests()
//                .antMatchers("/*").permitAll()
//        ;
//        http.exceptionHandling().authenticationEntryPoint(loginEntryPoint());

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true");

    }
}

//extends AbstractSecurityWebApplicationInitializer