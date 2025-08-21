package com.vipinSpring6.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf-> csrf.disable())
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("register").permitAll() // bs yeh permit yeh (bypass)
                                .anyRequest().authenticated() // rest all authenticate hai
                )
                .httpBasic(Customizer.withDefaults());

        return httpSecurity.build();
    }

    //@Bean - here we were using harcoded users details only these will allow to login
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


}
