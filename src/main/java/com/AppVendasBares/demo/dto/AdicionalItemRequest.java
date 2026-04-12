package com.AppVendasBares.demo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AdicionalItemRequest(
        @NotNull Long adicionalId,
        @NotNull @Min(1) Integer quantidade
) {}
