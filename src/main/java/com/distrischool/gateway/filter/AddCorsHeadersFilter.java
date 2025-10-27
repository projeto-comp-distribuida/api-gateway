package com.distrischool.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * Global filter to add CORS headers to gateway responses.
 * This runs AFTER RemoveDownstreamCorsFilter to ensure CORS headers are added
 * back to the response after downstream CORS headers have been removed.
 */
@Component
@Order(1) // Run after RemoveDownstreamCorsFilter (which has order -1)
public class AddCorsHeadersFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String origin = exchange.getRequest().getHeaders().getFirst(HttpHeaders.ORIGIN);
        
        // List of allowed origins
        List<String> allowedOrigins = Arrays.asList(
            "http://192.168.1.7:3000",
            "http://192.168.1.7:3001",
            "http://192.168.1.7:5173",
            "http://localhost:3000",
            "http://localhost:3001",
            "http://localhost:5173"
        );
        
        // Check if origin is allowed
        String allowedOrigin = origin;
        if (allowedOrigin == null || !allowedOrigins.contains(origin)) {
            allowedOrigin = "*"; // Allow all for development
        }
        
        // Wrap the response to add CORS headers
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = super.getHeaders();
                
                // Add CORS headers if not already present
                if (!headers.containsKey(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)) {
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE,OPTIONS,PATCH");
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "*");
                    headers.add(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
                    headers.add(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600");
                }
                
                return headers;
            }
        };
        
        ServerWebExchange modifiedExchange = exchange.mutate()
            .response(decoratedResponse)
            .build();
        
        return chain.filter(modifiedExchange);
    }

    @Override
    public int getOrder() {
        return 1; // Run after RemoveDownstreamCorsFilter (order -1)
    }
}

