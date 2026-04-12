package com.AppVendasBares.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistroClienteRequest(
        Long empresaId,
        @NotBlank String nome,
        @NotBlank String cpf,
        @NotBlank @Email String email,
        String telefone,
        @NotBlank
        @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres")
        @Pattern(regexp = ".*[A-Z].*", message = "A senha deve conter pelo menos 1 letra maiúscula")
        @Pattern(regexp = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*", message = "A senha deve conter pelo menos 1 caractere especial")
        String senha
) {}
