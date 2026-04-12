package com.AppVendasBares.demo.dto;

import java.math.BigDecimal;
import java.util.List;

public record PedidoItemResponse(
        Long id,
        String produtoNome,
        Integer quantidade,
        String observacao,
        String setor,
        String status,
        BigDecimal subtotal,
        List<AdicionalItemResponse> adicionais
) {}
