package com.AppVendasBares.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(
        BigDecimal faturamentoDiario,
        BigDecimal faturamentoSemanal,
        BigDecimal faturamentoMensal,
        BigDecimal ticketMedio,
        int totalPedidosHoje,
        int mesasOcupadas,
        int mesasLivres,
        List<ProdutoVendidoResponse> produtosMaisVendidos
) {}
