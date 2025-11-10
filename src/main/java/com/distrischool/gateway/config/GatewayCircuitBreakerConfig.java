package com.distrischool.gateway.config;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.timelimiter.TimeLimiterConfig;

/**
 * Circuit Breaker Configuration for API Gateway
 * 
 * This configuration sets up Resilience4J circuit breakers for protecting
 * the gateway against downstream service failures.
 * 
 * Features:
 * - Automatic circuit opening when failure threshold is exceeded
 * - Configurable failure rate thresholds
 * - Half-open state for gradual recovery testing
 * - Timeout protection for slow responses
 */
@Configuration
public class GatewayCircuitBreakerConfig {

    /**
     * Customizes the Resilience4J circuit breaker factory
     * with default settings for all circuit breakers
     */
    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> {
            return new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.custom()
                    // Percentage of failed calls to open the circuit
                    .failureRateThreshold(50)
                    // Number of calls to evaluate failure rate (sliding window)
                    .slidingWindowSize(10)
                    // Minimum number of calls before circuit can open (set to 1 for immediate response)
                    .minimumNumberOfCalls(1)
                    // Time to wait before attempting to close the circuit (half-open state)
                    .waitDurationInOpenState(Duration.ofSeconds(30))
                    // Number of calls allowed in half-open state
                    .permittedNumberOfCallsInHalfOpenState(3)
                    // By default, Resilience4J records ALL exceptions as failures
                    // We don't specify recordExceptions or ignoreExceptions, so all exceptions are tracked
                    // This includes connection errors (ConnectException, IOException), timeouts, etc.
                    // Consider slow calls as failures if they exceed this duration
                    .slowCallDurationThreshold(Duration.ofSeconds(5))
                    // If slow call rate exceeds this threshold, open the circuit
                    .slowCallRateThreshold(100)
                    .build())
                .timeLimiterConfig(TimeLimiterConfig.custom()
                    // Maximum time to wait for a response
                    .timeoutDuration(Duration.ofSeconds(10))
                    .build())
                .build();
        });
    }
}

