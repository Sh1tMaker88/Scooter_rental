package com.scooterrental.scooter_rental.configuration;

import com.scooterrental.scooter_rental.security.exception.JwtAuthenticationException;
import com.scooterrental.scooter_rental.security.jwt.JwtConfigurer;
import com.scooterrental.scooter_rental.security.jwt.JwtTokenProvider;
import org.n52.jackson.datatype.jts.JtsModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.support.WebStack;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebSecurity
//@EnableHypermediaSupport(stacks = WebStack.WEBMVC, type = EnableHypermediaSupport.HypermediaType.HAL)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public WebSecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//            .and()
//                .formLogin()
//                .loginPage("/login")
//                .permitAll()
            .and()
                .authorizeRequests()
                .antMatchers("/login", "/registration").permitAll()
                .antMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }

    @Bean
    public JtsModule jtsModule(){
        return new JtsModule();
    }
}
