package com.apibank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferenciaRequestDTO {

    @NotNull(message = "El campo 'cuenta_origen_id' es requerido")
    private Long cuenta_origen_id;

    @NotNull(message = "El campo 'cuenta_destino_id' es requerido")
    private Long cuenta_destino_id;

    @NotNull(message = "El campo 'monto' es requerido")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
}
