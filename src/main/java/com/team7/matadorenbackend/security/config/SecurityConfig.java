package com.team7.matadorenbackend.security.config;

import com.team7.matadorenbackend.appuser.AppUserService;
import com.team7.matadorenbackend.security.filter.CustomAuthenticateFilter;
import com.team7.matadorenbackend.security.filter.CustomAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AppUserService appUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticateFilter customAuthenticateFilter = new CustomAuthenticateFilter(authenticationManagerBean());
        http.csrf().disable().
                sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/register/user/save/**").permitAll()
                .antMatchers("login", "/token/refresh/**").permitAll()
                .antMatchers(HttpMethod.GET,"/register/user/get/**").hasAnyAuthority("ADMIN","USER")
               //.antMatchers(HttpMethod.GET,"/register/user/get/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.PUT,"/register/user/update/**").hasAnyAuthority("ADMIN","USER")
               // .antMatchers(HttpMethod.PUT,"/register/user/update/**").hasAnyAuthority("USER")
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.DELETE, "/register/user/delete/**")
                .hasAnyAuthority("ADMIN").and()
                .authorizeRequests()
                .anyRequest()
                .authenticated().and()
                .addFilter(customAuthenticateFilter)
                .addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .httpBasic()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.ACCEPTED))
                .deleteCookies();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(appUserService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
