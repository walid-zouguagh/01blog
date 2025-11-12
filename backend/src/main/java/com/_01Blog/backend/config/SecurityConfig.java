package com._01Blog.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // on donne que ce class est un main de sping security
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    // private final UserRepository userRepository;

    // Start configuration : open endpoint, close endpoint ... all configurations
    @Bean // Bean for injection a methods, Implementation for injection
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable()) // Disabled cookie : remplace cookie instand of jwt
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/register").permitAll()
                        .requestMatchers("/api/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    // var user = userRepository.findByEmail(email)
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    // return User.builder()
    // .username(user.getEmail())
    // .password(user.getPassword())
    // .roles(user.getRole().name())
    // .build();
    // }

    // public UserDetailsService userDetailsService() {
    // return email -> userRepository.findByEmail(email)
    // .map(user -> org.springframework.security.core.userdetails.User
    // .withUsername(user.getUsername())
    // .password(user.getPassword())
    // .roles(user.getRole().toUpperCase())
    // .build())
    // .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    // }
}
