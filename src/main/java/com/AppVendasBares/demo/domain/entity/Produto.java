package com.AppVendasBares.demo.domain.entity;

import com.AppVendasBares.demo.domain.enums.SetorProducao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produto")
@Getter
@Setter
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaProduto categoria;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(length = 255)
    private String foto;

    @Enumerated(EnumType.STRING)
    @Column(name = "setor_producao", nullable = false, length = 30)
    private SetorProducao setorProducao;

    @Column(nullable = false)
    private Integer estoque = 0;

    @Column(nullable = false)
    private Boolean ativo = true;
}