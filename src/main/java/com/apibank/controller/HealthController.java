package com.apibank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    /**
     * GET /api/v1/health-check
     * Equivalent to Node.js app.get("/api/v1/health-check")
     */
    @GetMapping("/health-check")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "Online",
                "health", "ok"));
    }

    /**
     * GET /api/v1/mensaje
     * Equivalent to Node.js app.get("/api/v1/mensaje")
     */
    @GetMapping("/mensaje")
    public ResponseEntity<Map<String, String>> mensaje() {
        return ResponseEntity.ok(Map.of(
                "name", "Romy",
                "lastName", "Cardozo"));
    }

    /**
     * GET /api/v1/saludar
     * Equivalent to Node.js app.get("/api/v1/saludar")
     */
    @GetMapping("/saludar")
    public ResponseEntity<String> saludar() {
        return ResponseEntity.ok("Holi");
    }
}
