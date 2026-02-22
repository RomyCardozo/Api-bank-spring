package com.apibank.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cuentas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId;

    @Column(name = "tipo_cuenta_id", nullable = false)
    private Integer tipoCuentaId;

    @Column(name = "numero_cuenta", nullable = false, length = 20)
    private String numeroCuenta;

    @Column(name = "saldo", nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relations
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", insertable = false, updatable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cuenta_id", insertable = false, updatable = false)
    private TipoCuenta tipoCuenta;

    @OneToMany(mappedBy = "cuentaOrigen", fetch = FetchType.LAZY)
    private List<Transferencia> transferenciasOrigen;

    @OneToMany(mappedBy = "cuentaDestino", fetch = FetchType.LAZY)
    private List<Transferencia> transferenciasDestino;

    @OneToMany(mappedBy = "cuenta", fetch = FetchType.LAZY)
    private List<Transaccion> transacciones;

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
