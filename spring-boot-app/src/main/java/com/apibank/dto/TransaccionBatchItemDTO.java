package com.apibank.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionBatchItemDTO {

    @NotNull(message = "El campo 'cuenta_id' es requerido")
    private Long cuenta_id;

    @NotNull(message = "El campo 'tipo' es requerido")
    private String tipo;

    @NotNull(message = "El campo 'monto' es requerido")
    @Positive(message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
}
