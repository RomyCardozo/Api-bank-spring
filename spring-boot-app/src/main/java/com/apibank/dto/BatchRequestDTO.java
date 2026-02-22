package com.apibank.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BatchRequestDTO {

    @NotNull(message = "El campo 'transacciones' es requerido")
    @Valid
    private List<TransaccionBatchItemDTO> transacciones;
}
