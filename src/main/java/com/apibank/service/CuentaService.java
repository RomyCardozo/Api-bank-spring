package com.apibank.service;

import com.apibank.dto.CuentaRequestDTO;
import com.apibank.dto.CuentaResponseDTO;
import com.apibank.entity.Cuenta;
import com.apibank.exception.EntityNotFoundException;
import com.apibank.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    @Transactional
    public CuentaResponseDTO crearCuenta(CuentaRequestDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId(dto.getCliente_id());
        cuenta.setTipoCuentaId(dto.getTipo_cuenta_id());
        cuenta.setNumeroCuenta(dto.getNumero_cuenta());
        cuenta.setSaldo(dto.getSaldo());
        Cuenta saved = cuentaRepository.save(cuenta);
        return mapCuenta(saved);
    }

    @Transactional(readOnly = true)
    public CuentaResponseDTO obtenerCuentaPorId(Long id) {
        Cuenta c = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        return mapCuenta(c);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> obtenerCuentas() {
        return cuentaRepository.findAll().stream().map(this::mapCuenta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CuentaResponseDTO> obtenerCuentasPorCliente(Integer clienteId) {
        return cuentaRepository.findByClienteId(clienteId).stream().map(this::mapCuenta).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldo(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        return cuenta.getSaldo();
    }

    @Transactional
    public CuentaResponseDTO actualizarSaldo(Long id, BigDecimal nuevoSaldo) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        cuenta.setSaldo(nuevoSaldo);
        Cuenta saved = cuentaRepository.save(cuenta);
        return mapCuenta(saved);
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
