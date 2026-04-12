package com.AppVendasBares.demo.dto;

public record MesaRequest(
        Long empresaId,
        Integer numero,
        String nome,
        String area,
        String setor,
        String andar,
        String tipoMesa,
        Integer qtdCadeiras,
        Integer posicaoX,
        Integer posicaoY
) {}