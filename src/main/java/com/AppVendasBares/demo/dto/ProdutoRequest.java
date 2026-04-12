package com.AppVendasBares.demo.dto;

import com.AppVendasBares.demo.domain.enums.SetorProducao;
import java.math.BigDecimal;

public record ProdutoRequest(
        Long empresaId,
        Long categoriaId,
        String nome,
        String descricao,
        BigDecimal preco,
        String foto,
        SetorProducao setorProducao,
        Integer estoque
) {}