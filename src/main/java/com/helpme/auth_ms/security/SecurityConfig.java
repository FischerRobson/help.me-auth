package com.helpme.auth_ms.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()  // Disable CSRF protection for stateless APIs
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/users/**", "/auth/**").permitAll()
//                        .anyRequest().authenticated())  // All other requests require authentication
//                .httpBasic().disable()  // Disable basic authentication
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Ensure the application doesn't maintain any session
//
//        // Further configuration for JWT here if necessary
//
//        return http.build();
//    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/users", "/auth/login").permitAll();
                    auth.anyRequest().authenticated();
                });
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
