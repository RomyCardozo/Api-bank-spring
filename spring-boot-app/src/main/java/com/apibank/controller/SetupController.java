package com.apibank.controller;

import com.apibank.service.SetupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/setup")
@RequiredArgsConstructor
public class SetupController {

    private final SetupService setupService;

    /**
     * POST /api/v1/setup?cantidad=10
     * Creates N test clients with accounts. Equivalent to Node.js POST /setup.
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> crearDataset(
            @RequestParam(defaultValue = "10") int cantidad) {

        Map<String, Object> result = setupService.crearDataset(cantidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
