package com.apibank.service;

import com.apibank.entity.TipoCuenta;
import com.apibank.exception.EntityNotFoundException;
import com.apibank.repository.TipoCuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCuentaService {

    private final TipoCuentaRepository tipoCuentaRepository;

    @Transactional
    public TipoCuenta crearTipoCuenta(TipoCuenta tipoCuenta) {
        return tipoCuentaRepository.save(tipoCuenta);
    }

    @Transactional(readOnly = true)
    public List<TipoCuenta> obtenerTiposCuentas() {
        return tipoCuentaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public TipoCuenta obtenerTipoCuentaPorId(Integer id) {
        return tipoCuentaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de cuenta no encontrado"));
    }
}
