package com._01Blog.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // on donne que ce class est un main de sping security
public class SecurityConfig {

    // Start configuration : open endpoint, close endpoint ... all configurations
    @Bean // Bean for injection a methods, Implementation for injection
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .cors(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> {
                auth.requestMatchers(
                    "/auth/login"
                ).permitAll();
            });

        return http.build();

    }
}
