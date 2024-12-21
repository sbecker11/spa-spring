package com.spexture.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spexture.service.UserService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public UserService userService() {
        logger.info("Initializing UserService bean");
        return new UserService(); // Assuming UserService is your UserDetailsService implementation
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        logger.info("Initializing BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring security filter chain");
        http
            .authorizeHttpRequests((requests) -> { 
                requests // these pages require no authentication
                    .requestMatchers("/","/home","/about", "/register", "/webjars/**", "/css/**").permitAll(); // Allow access to /home without authentication
                logger.info("handled request NOT requirming authentication");
                    requests // any other pages require authentication
                    .anyRequest().authenticated(); 
                logger.info("handled request requirming authentication");

            })
            // .formLogin((form) -> {
            //     logger.info("Configuring form login");
            //     form
            //         .loginPage("/login")
            //         .defaultSuccessUrl("/home", true) // Redirect to /home after successful login
            //         .permitAll();
            // })
            // .oauth2Login((oauth2) -> {
            //     logger.info("Configuring oauth2 login");
            //     oauth2.loginPage("/login");
            // })
            .logout((logout) -> {
                logger.info("Configuring logout");
                logout.permitAll();
            });
        logger.info("Security configuration applied successfully");
        return http.build();
    }
}