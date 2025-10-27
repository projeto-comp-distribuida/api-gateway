package com.distrischool.gateway.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * CORS Configuration for API Gateway
 * 
 * This configuration ensures proper handling of CORS preflight (OPTIONS) requests
 * and allows cross-origin requests from the frontend applications.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        
        // Allow credentials (cookies, authorization headers)
        corsConfiguration.setAllowCredentials(true);
        
        // Allowed origins
        corsConfiguration.setAllowedOrigins(Arrays.asList(
            "http://192.168.1.7:3000",
            "http://192.168.1.7:5173",
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:5173"
        ));
        
        // Allowed HTTP methods
        corsConfiguration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD"
        ));
        
        // Allowed headers - allow all for maximum compatibility
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        
        // Exposed headers
        corsConfiguration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials",
            "Authorization",
            "Content-Type"
        ));
        
        // Cache preflight response for 1 hour
        corsConfiguration.setMaxAge(3600L);
        
        // Apply CORS configuration to all paths
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        
        return new CorsWebFilter(source);
    }
}

