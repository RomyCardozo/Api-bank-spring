package com.apibank.controller;

import com.apibank.entity.TipoCuenta;
import com.apibank.service.TipoCuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-cuentas")
@RequiredArgsConstructor
public class TipoCuentaController {

    private final TipoCuentaService tipoCuentaService;

    /** POST /api/v1/tipos-cuentas */
    @PostMapping
    public ResponseEntity<TipoCuenta> crearTipoCuenta(@RequestBody TipoCuenta tipoCuenta) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoCuentaService.crearTipoCuenta(tipoCuenta));
    }

    /** GET /api/v1/tipos-cuentas */
    @GetMapping
    public ResponseEntity<List<TipoCuenta>> obtenerTiposCuentas() {
        return ResponseEntity.ok(tipoCuentaService.obtenerTiposCuentas());
    }

    /** GET /api/v1/tipos-cuentas/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<TipoCuenta> obtenerTipoCuentaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoCuentaService.obtenerTipoCuentaPorId(id));
    }
}
