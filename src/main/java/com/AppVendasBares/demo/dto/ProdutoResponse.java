package com.AppVendasBares.demo.dto;

import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        String categoria,
        String setorProducao,
        Integer estoque,
        Boolean ativo
) {}