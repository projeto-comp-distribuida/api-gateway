package com.distrischool.gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Fallback controller for circuit breaker scenarios
 * Provides graceful degradation when microservices are unavailable
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/auth")
    public Mono<ResponseEntity<Map<String, Object>>> authFallback() {
        return Mono.just(createFallbackResponse("Auth service is temporarily unavailable"));
    }

    @GetMapping("/students")
    public Mono<ResponseEntity<Map<String, Object>>> studentsFallback() {
        return Mono.just(createFallbackResponse("Student service is temporarily unavailable"));
    }

    @GetMapping("/teachers")
    public Mono<ResponseEntity<Map<String, Object>>> teachersFallback() {
        return Mono.just(createFallbackResponse("Teacher service is temporarily unavailable"));
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Service Unavailable");
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}
