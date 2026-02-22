package com.apibank.controller;

import com.apibank.dto.ClienteResponseDTO;
import com.apibank.entity.Cliente;
import com.apibank.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    /** POST /api/v1/clientes */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> crearCliente(@RequestBody Cliente cliente) {
        ClienteResponseDTO nuevo = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    /** GET /api/v1/clientes */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> obtenerClientes() {
        return ResponseEntity.ok(clienteService.obtenerClientes());
    }

    /** GET /api/v1/clientes/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> obtenerClientePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }
}
