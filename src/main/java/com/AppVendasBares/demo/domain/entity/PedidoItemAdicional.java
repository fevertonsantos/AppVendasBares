package com.AppVendasBares.demo.domain.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pedido_item_adicional")
public class PedidoItemAdicional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_item_id")
    private PedidoItem pedidoItem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "adicional_id")
    private Adicional adicional;

    @Column(nullable = false)
    private Integer quantidade = 1;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    public PedidoItemAdicional() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PedidoItem getPedidoItem() { return pedidoItem; }
    public void setPedidoItem(PedidoItem pedidoItem) { this.pedidoItem = pedidoItem; }

    public Adicional getAdicional() { return adicional; }
    public void setAdicional(Adicional adicional) { this.adicional = adicional; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public BigDecimal getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(BigDecimal precoUnitario) { this.precoUnitario = precoUnitario; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
