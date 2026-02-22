package com.apibank.controller;

import com.apibank.dto.TransferenciaRequestDTO;
import com.apibank.dto.TransferenciaResponseDTO;
import com.apibank.service.TransferenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private final TransferenciaService transferenciaService;

    /**
     * POST /api/v1/transferencias
     * Equivalent to Node.js POST /transferencias (crearTransferencia).
     * @Valid triggers Bean Validation on the DTO; errors are handled by GlobalExceptionHandler.
     */
    @PostMapping
    public ResponseEntity<TransferenciaResponseDTO> crearTransferencia(
            @Valid @RequestBody TransferenciaRequestDTO request) {

        TransferenciaResponseDTO result = transferenciaService.crearTransferencia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * GET /api/v1/transferencias/{id}
     * Equivalent to Node.js GET /transferencias/:id
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaResponseDTO> obtenerTransferenciaById(@PathVariable Long id) {
        return ResponseEntity.ok(transferenciaService.obtenerTransferenciaPorId(id));
    }
}
