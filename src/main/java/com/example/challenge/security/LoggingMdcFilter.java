package com.example.challenge.security;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class LoggingMdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            String traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);

            String userId = (String) httpRequest.getAttribute("userId");
            if (userId != null) {
                MDC.put("userId", userId);
            }

            String sessionId = (String) httpRequest.getAttribute("sessionId");
            if (userId != null) {
                MDC.put("sessionId", sessionId);
            }

            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}