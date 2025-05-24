package com.example.admin.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    
    
    
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username;

            try {
                username = jwtUtil.extractUsername(jwt);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    } else {
                        // Token is invalid or expired
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                        return;
                    }
                }

            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                // Token is expired
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expired");
                return;
            } catch (Exception e) {
                // Other JWT exceptions
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }    
    
    
    
	/*
	 * @Override protected void doFilterInternal(HttpServletRequest request,
	 * HttpServletResponse response, FilterChain filterChain) throws
	 * ServletException, IOException { String authHeader =
	 * request.getHeader("Authorization");
	 * 
	 * if (authHeader != null && authHeader.startsWith("Bearer ")) { String jwt =
	 * authHeader.substring(7); String username = jwtUtil.extractUsername(jwt);
	 * 
	 * if (username != null &&
	 * SecurityContextHolder.getContext().getAuthentication() == null) { UserDetails
	 * userDetails = userDetailsService.loadUserByUsername(username);
	 * UsernamePasswordAuthenticationToken authToken = new
	 * UsernamePasswordAuthenticationToken(userDetails, null,
	 * userDetails.getAuthorities());
	 * 
	 * SecurityContextHolder.getContext().setAuthentication(authToken);
	 * 
	 * 
	 * } }
	 * 
	 * filterChain.doFilter(request, response); }
	 */
}


