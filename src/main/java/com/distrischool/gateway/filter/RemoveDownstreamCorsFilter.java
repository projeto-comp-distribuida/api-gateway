package com.distrischool.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Global filter to remove CORS headers from downstream services
 * to prevent "Multiple Origin Not Allowed" errors.
 * Only the API Gateway should handle CORS.
 */
@Component
public class RemoveDownstreamCorsFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Remove Origin header from request to prevent downstream services from rejecting based on CORS
        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
            .headers(httpHeaders -> httpHeaders.remove("Origin"))
            .build();
        
        ServerWebExchange modifiedExchange = exchange.mutate()
            .request(modifiedRequest)
            .build();
        
        return chain.filter(modifiedExchange).then(Mono.fromRunnable(() -> {
            var response = modifiedExchange.getResponse();
            var headers = response.getHeaders();
            
            // Remove all CORS headers from downstream services
            headers.remove("Access-Control-Allow-Origin");
            headers.remove("Access-Control-Allow-Credentials");
            headers.remove("Access-Control-Expose-Headers");
            headers.remove("Access-Control-Max-Age");
            headers.remove("Access-Control-Allow-Methods");
            headers.remove("Access-Control-Allow-Headers");
        }));
    }

    @Override
    public int getOrder() {
        return -1; // Execute before other filters
    }
}


