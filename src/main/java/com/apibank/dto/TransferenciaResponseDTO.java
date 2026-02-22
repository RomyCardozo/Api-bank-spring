package com.apibank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferenciaResponseDTO {

    private Long id;
    private Long cuenta_origen_id;
    private Long cuenta_destino_id;
    private BigDecimal monto;
    private LocalDateTime fecha;
}
