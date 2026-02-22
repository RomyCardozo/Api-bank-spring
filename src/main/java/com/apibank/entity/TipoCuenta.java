package com.apibank.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Entity
@Table(name = "tipos_cuenta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @OneToMany(mappedBy = "tipoCuenta", fetch = FetchType.LAZY)
    private List<Cuenta> cuentas;
}
