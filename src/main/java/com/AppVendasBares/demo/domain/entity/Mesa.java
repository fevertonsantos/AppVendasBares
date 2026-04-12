package com.AppVendasBares.demo.domain.entity;

import com.AppVendasBares.demo.domain.enums.StatusMesa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "mesa")
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @Column(nullable = false)
    private Integer numero;

    @Column(length = 80)
    private String nome;

    @Column(length = 50)
    private String area;

    @Column(length = 50)
    private String setor;

    @Column(length = 50)
    private String andar;

    @Column(name = "tipo_mesa", length = 50)
    private String tipoMesa;

    @Column(name = "qtd_cadeiras")
    private Integer qtdCadeiras;

    @Column(name = "posicao_x")
    private Integer posicaoX;

    @Column(name = "posicao_y")
    private Integer posicaoY;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusMesa status = StatusMesa.LIVRE;

    public Mesa() {
    }

    public Long getId() {
        return id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getAndar() {
        return andar;
    }

    public void setAndar(String andar) {
        this.andar = andar;
    }

    public String getTipoMesa() {
        return tipoMesa;
    }

    public void setTipoMesa(String tipoMesa) {
        this.tipoMesa = tipoMesa;
    }

    public Integer getQtdCadeiras() {
        return qtdCadeiras;
    }

    public void setQtdCadeiras(Integer qtdCadeiras) {
        this.qtdCadeiras = qtdCadeiras;
    }

    public Integer getPosicaoX() {
        return posicaoX;
    }

    public void setPosicaoX(Integer posicaoX) {
        this.posicaoX = posicaoX;
    }

    public Integer getPosicaoY() {
        return posicaoY;
    }

    public void setPosicaoY(Integer posicaoY) {
        this.posicaoY = posicaoY;
    }

    public StatusMesa getStatus() {
        return status;
    }

    public void setStatus(StatusMesa status) {
        this.status = status;
    }
}