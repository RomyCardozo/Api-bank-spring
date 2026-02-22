package com.apibank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CuentaRequestDTO {

    @NotNull(message = "El campo 'cliente_id' es requerido")
    private Integer cliente_id;

    @NotNull(message = "El campo 'tipo_cuenta_id' es requerido")
    private Integer tipo_cuenta_id;

    @NotNull(message = "El campo 'numero_cuenta' es requerido")
    private String numero_cuenta;

    @NotNull(message = "El campo 'saldo' es requerido")
    @Positive(message = "El saldo debe ser mayor o igual a cero")
    private BigDecimal saldo;
}
