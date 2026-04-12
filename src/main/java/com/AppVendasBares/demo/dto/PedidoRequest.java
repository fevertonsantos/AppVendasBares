package com.AppVendasBares.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoRequest(
        @NotNull Long empresaId,
        @NotNull Long mesaId,
        Long usuarioId,
        Long garcomId,
        @NotEmpty List<PedidoItemRequest> itens
) {}
