package com.apibank.service;

import com.apibank.dto.ClienteResponseDTO;
import com.apibank.dto.CuentaResponseDTO;
import com.apibank.entity.Cliente;
import com.apibank.entity.Cuenta;
import com.apibank.exception.EntityNotFoundException;
import com.apibank.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponseDTO crearCliente(Cliente cliente) {
        Cliente nuevo = clienteRepository.save(cliente);
        return mapCliente(nuevo);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> obtenerClientes() {
        return clienteRepository.findAll().stream()
                .map(this::mapCliente)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO obtenerClientePorId(Integer id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));
        return mapCliente(c);
    }

    private ClienteResponseDTO mapCliente(Cliente c) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(c.getId());
        dto.setNombre(c.getNombre());
        dto.setEmail(c.getEmail());
        dto.setCreatedAt(c.getCreatedAt());
        if (c.getCuentas() != null) {
            dto.setCuentas(c.getCuentas().stream().map(this::mapCuenta).collect(Collectors.toList()));
        }
        return dto;
    }

    private CuentaResponseDTO mapCuenta(Cuenta c) {
        if (c == null) return null;
        CuentaResponseDTO dto = new CuentaResponseDTO();
        dto.setId(c.getId());
        dto.setClienteId(c.getClienteId());
        dto.setTipoCuentaId(c.getTipoCuentaId());
        dto.setNumeroCuenta(c.getNumeroCuenta());
        dto.setSaldo(c.getSaldo());
        dto.setCreatedAt(c.getCreatedAt());
        return dto;
    }
}
