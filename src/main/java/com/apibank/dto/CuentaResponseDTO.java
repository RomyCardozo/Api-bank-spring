package com.apibank.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CuentaResponseDTO {
    private Long id;
    private Integer clienteId;
    private Integer tipoCuentaId;
    private String numeroCuenta;
    private BigDecimal saldo;
    private LocalDateTime createdAt;
}
