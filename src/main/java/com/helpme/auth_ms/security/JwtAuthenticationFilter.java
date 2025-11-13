package com.helpme.auth_ms.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.helpme.auth_ms.constants.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    Constants constants;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractTokenFromCookies(request);

        if (token != null) {
            DecodedJWT decodedJWT = validateAndDecodeToken(token);
            if (decodedJWT != null) {
                String email = decodedJWT.getClaim("email").asString();
                String role = decodedJWT.getClaim("role").asString();
                String userId = decodedJWT.getSubject();

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);

                request.setAttribute("userEmail", email);
                request.setAttribute("userRole", role);
                request.setAttribute("userId", userId);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("jwt".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        logger.error("Failed to get jwt cookie from request");
        return null;
    }

    private DecodedJWT validateAndDecodeToken(String token) {
        try {
            return JWT.require(constants.getAlgorithm())
                    .withIssuer("helpme-auth")
                    .build()
                    .verify(token);
        } catch (Exception e) {
            logger.warn("JWT validation failed: {}", e.getMessage());
            return null;
        }
    }
}