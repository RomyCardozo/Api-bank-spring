package com.apibank.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transferencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cuenta_origen_id", nullable = false)
    private Long cuentaOrigenId;

    @Column(name = "cuenta_destino_id", nullable = false)
    private Long cuentaDestinoId;

    @Column(name = "monto", nullable = false, precision = 15, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false, updatable = false)
    private LocalDateTime fecha;

    // Relations – named to match Prisma's "CuentaOrigen" / "CuentaDestino"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_origen_id", insertable = false, updatable = false)
    private Cuenta cuentaOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_destino_id", insertable = false, updatable = false)
    private Cuenta cuentaDestino;

    @PrePersist
    protected void onCreate() {
        if (this.fecha == null) {
            this.fecha = LocalDateTime.now();
        }
    }
}
