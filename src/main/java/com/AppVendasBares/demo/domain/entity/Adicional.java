package com.AppVendasBares.demo.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "adicional")
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

    public Adicional() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getQtdInclusa() { return qtdInclusa; }
    public void setQtdInclusa(Integer qtdInclusa) { this.qtdInclusa = qtdInclusa; }

    public Integer getQtdMaxima() { return qtdMaxima; }
    public void setQtdMaxima(Integer qtdMaxima) { this.qtdMaxima = qtdMaxima; }

    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
}
