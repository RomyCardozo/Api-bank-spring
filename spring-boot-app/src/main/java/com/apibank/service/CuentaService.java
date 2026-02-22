package com.apibank.service;

import com.apibank.dto.CuentaRequestDTO;
import com.apibank.entity.Cuenta;
import com.apibank.exception.EntityNotFoundException;
import com.apibank.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    @Transactional
    public Cuenta crearCuenta(CuentaRequestDTO dto) {
        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId(dto.getCliente_id());
        cuenta.setTipoCuentaId(dto.getTipo_cuenta_id());
        cuenta.setNumeroCuenta(dto.getNumero_cuenta());
        cuenta.setSaldo(dto.getSaldo());
        return cuentaRepository.save(cuenta);
    }

    @Transactional(readOnly = true)
    public Cuenta obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentas() {
        return cuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Cuenta> obtenerCuentasPorCliente(Integer clienteId) {
        return cuentaRepository.findByClienteId(clienteId);
    }

    @Transactional(readOnly = true)
    public BigDecimal obtenerSaldo(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        return cuenta.getSaldo();
    }

    @Transactional
    public Cuenta actualizarSaldo(Long id, BigDecimal nuevoSaldo) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cuenta no encontrada"));
        cuenta.setSaldo(nuevoSaldo);
        return cuentaRepository.save(cuenta);
    }
}
