package com.AppVendasBares.demo.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "adicional")
@Getter
@Setter
public class Adicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco = BigDecimal.ZERO;

    @Column(name = "qtd_inclusa", nullable = false)
    private Integer qtdInclusa = 0;

    @Column(name = "qtd_maxima", nullable = false)
    private Integer qtdMaxima = 1;

    @Column(nullable = false)
    private Boolean ativo = true;
}