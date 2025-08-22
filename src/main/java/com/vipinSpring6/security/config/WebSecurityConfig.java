package com.vipinSpring6.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserDetailsService userDetailsService;

    public WebSecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("register", "login").permitAll() // bs yeh permit yeh (bypass)
                                .anyRequest().authenticated() // rest all authenticate hai
                )
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    //@Bean - here we were using harcoded users details only these will allow to login but not this USED
    public UserDetailsService userDetailsService(){

        UserDetails vipin
                = User.withUsername("vipin")
                .password("{noop}shivhare")
                .roles("USER")
                .build();

        UserDetails vipin1
                = User.withUsername("vipin1")
                .password("{noop}shivhare1")
                .roles("USER")
                .build();


       return new InMemoryUserDetailsManager(vipin, vipin1);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder(14);
    }



    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }
}
