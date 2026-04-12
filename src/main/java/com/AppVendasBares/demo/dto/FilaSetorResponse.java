package com.AppVendasBares.demo.dto;

import java.util.List;

public record FilaSetorResponse(
        String setor,
        int emFila,
        int emPreparo,
        int prontos,
        int atrasados,
        List<PedidoItemResponse> itens
) {}
