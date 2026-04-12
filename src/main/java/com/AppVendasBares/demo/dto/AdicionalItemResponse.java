package com.AppVendasBares.demo.dto;

import java.math.BigDecimal;

public record AdicionalItemResponse(
        String nome,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal
) {}
