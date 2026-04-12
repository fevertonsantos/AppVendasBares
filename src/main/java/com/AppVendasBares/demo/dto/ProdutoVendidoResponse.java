package com.AppVendasBares.demo.dto;

public record ProdutoVendidoResponse(
        Long produtoId,
        String nome,
        long totalVendido
) {}
