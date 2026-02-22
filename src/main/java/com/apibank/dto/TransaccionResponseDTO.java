package com.apibank.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionResponseDTO {
    private Long id;
    private Long cuenta_id;
    private String tipo;
    private BigDecimal monto;
    private LocalDateTime fecha;
}
