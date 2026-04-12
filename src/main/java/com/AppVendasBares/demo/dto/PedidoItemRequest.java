package com.AppVendasBares.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoItemRequest(
        @NotNull Long produtoId,
        @NotNull @Min(1) Integer quantidade,
        String observacao,
        List<AdicionalItemRequest> adicionais
) {}
