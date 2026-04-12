package com.AppVendasBares.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long mesaId,
        Integer mesaNumero,
        String nomeCliente,
        String nomeGarcom,
        String status,
        BigDecimal total,
        Integer tempoEstimado,
        LocalDateTime criadoEm,
        List<PedidoItemResponse> itens
) {}
