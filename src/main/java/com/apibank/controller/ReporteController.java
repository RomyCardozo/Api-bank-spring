package com.apibank.controller;

import com.apibank.entity.Transaccion;
import com.apibank.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final TransaccionService transaccionService;

    /**
     * GET /api/v1/reportes/transacciones
     * Supports: ?desde=, ?hasta=, ?limit=, ?offset=
     * Equivalent to Node.js GET /reportes + reporteTransacciones controller.
     */
    @GetMapping("/transacciones")
    public ResponseEntity<List<Transaccion>> reporteTransacciones(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,

            @RequestParam(defaultValue = "1000") int limit,
            @RequestParam(defaultValue = "0") int offset) {
        var page = transaccionService.obtenerTransacciones(desde, hasta, null, limit, offset);
        return ResponseEntity.ok(page.getContent());
    }
}
