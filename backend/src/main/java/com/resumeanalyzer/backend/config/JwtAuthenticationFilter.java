package com.resumeanalyzer.backend.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil){
        this.jwtUtil=jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws
    ServletException, IOException{
        String authHeader=request.getHeader("Authorization");

        // If no bearer token is present, continue without authentication
        if(authHeader==null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token=authHeader.substring(7);

        if(jwtUtil.isTokenValid(token) && SecurityContextHolder.getContext().getAuthentication()==null){
            String email=jwtUtil.extractEmail(token);

            UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(
                email,
                null,
                Collections.emptyList()
            );

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // It will set authentication in SecurityContext, which we will later check at .anyRequest().authenticated() in SecurityFilterChain
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
