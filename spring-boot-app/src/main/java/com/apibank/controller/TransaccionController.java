package com.apibank.controller;

import com.apibank.dto.BatchRequestDTO;
import com.apibank.entity.Transaccion;
import com.apibank.service.TransaccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/transacciones")
@RequiredArgsConstructor
public class TransaccionController {

    private static final int MAX_OFFSET = 100_000;

    private final TransaccionService transaccionService;

    /**
     * GET /api/v1/transacciones
     * Supports: ?desde=, ?hasta=, ?cuenta_id=, ?limit=, ?offset=
     * Equivalent to Node.js listarTransacciones controller.
     */
    @GetMapping
    public ResponseEntity<?> listarTransacciones(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,

            @RequestParam(name = "cuenta_id", required = false) Long cuentaId,
            @RequestParam(defaultValue = "100") int limit,
            @RequestParam(defaultValue = "0") int offset
    ) {
        if (limit <= 0) limit = 100;
        if (offset < 0) offset = 0;
        if (offset > MAX_OFFSET) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Offset demasiado grande. Máximo permitido " + MAX_OFFSET));
        }

        var page = transaccionService.obtenerTransacciones(desde, hasta, cuentaId, limit, offset);
        return ResponseEntity.ok(page.getContent());
    }

    /**
     * POST /api/v1/transacciones/batch
     * Real bulk insert using saveAll() inside @Transactional.
     * Equivalent to Node.js batchInsertTransacciones -> prisma.transacciones.createMany().
     */
    @PostMapping("/batch")
    public ResponseEntity<Map<String, Object>> batchInsert(@Valid @RequestBody BatchRequestDTO request) {
        List<Transaccion> saved = transaccionService.crearBatchTransacciones(request.getTransacciones());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("count", saved.size()));
    }
}
