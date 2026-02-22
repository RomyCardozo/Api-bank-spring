package com.apibank.controller;

import com.apibank.dto.CuentaRequestDTO;
import com.apibank.entity.Cuenta;
import com.apibank.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    /** POST /api/v1/cuentas */
    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@RequestBody CuentaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crearCuenta(dto));
    }

    /**
     * GET /api/v1/cuentas
     * Supports optional ?cliente_id= query param to filter by client.
     */
    @GetMapping
    public ResponseEntity<List<Cuenta>> listarCuentas(
            @RequestParam(name = "cliente_id", required = false) Integer clienteId) {

        List<Cuenta> result = (clienteId != null)
                ? cuentaService.obtenerCuentasPorCliente(clienteId)
                : cuentaService.obtenerCuentas();

        return ResponseEntity.ok(result);
    }

    /** GET /api/v1/cuentas/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerCuentaById(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaPorId(id));
    }

    /**
     * GET /api/v1/cuentas/{id}/saldo
     * Equivalent to Node.js GET /cuentas/:id/saldo
     */
    @GetMapping("/{id}/saldo")
    public ResponseEntity<Map<String, Object>> obtenerSaldo(@PathVariable Long id) {
        BigDecimal saldo = cuentaService.obtenerSaldo(id);
        return ResponseEntity.ok(Map.of(
                "cuenta_id", id,
                "saldo", saldo
        ));
    }

    /**
     * PUT /api/v1/cuentas/{id}/saldo
     * Equivalent to Node.js PATCH /cuentas/:id (actualizar saldo)
     */
    @PutMapping("/{id}/saldo")
    public ResponseEntity<Cuenta> actualizarSaldo(
            @PathVariable Long id,
            @RequestBody Map<String, BigDecimal> body) {

        BigDecimal nuevoSaldo = body.get("nuevoSaldo");
        if (nuevoSaldo == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(cuentaService.actualizarSaldo(id, nuevoSaldo));
    }
}
