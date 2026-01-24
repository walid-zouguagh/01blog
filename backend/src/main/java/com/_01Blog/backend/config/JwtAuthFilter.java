package com._01Blog.backend.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com._01Blog.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    // read token from request

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        System.err.println("DEBUG: Checking shouldNotFilter for URI: " + request.getRequestURI());
        String path = request.getRequestURI();
        return path.startsWith("/auth/login") || path.startsWith("/auth/register");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        System.err.println("DEBUG: Executing doFilterInternal for URI: " + request.getRequestURI());

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            System.err.println("DEBUG: No Authorization header or valid prefix found.");
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        System.err.println("DEBUG: Token found: " + token);

        String email = null;
        try {
            email = jwtService.extractUsername(token);
            System.err.println("DEBUG: Extracted email: " + email);
        } catch (Exception e) {
            System.err.println("DEBUG: Failed to extract email from token: " + e.getMessage());
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
            System.err.println("DEBUG: User loaded: " + userDetails.getUsername());

            if (jwtService.isTokenValid(token)) {
                System.err.println("DEBUG: Token is valid.");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.err.println("DEBUG: Authentication set for user: " + userDetails.getUsername()
                        + " with authorities: " + userDetails.getAuthorities());
                request.setAttribute("email", email);
                request.setAttribute("user", userDetails);
            } else {
                System.err.println("DEBUG: Token is INVALID.");
            }
        } else {
            System.err.println("DEBUG: Email null or Context already set. Email: " + email);
        }

        filterChain.doFilter(request, response);
    }

}
