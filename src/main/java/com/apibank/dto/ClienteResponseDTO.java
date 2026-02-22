package com.apibank.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ClienteResponseDTO {
    private Integer id;
    private String nombre;
    private String email;
    private LocalDateTime createdAt;
    private List<CuentaResponseDTO> cuentas;
}
