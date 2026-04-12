package com.AppVendasBares.demo.dto;

import com.AppVendasBares.demo.domain.enums.PerfilUsuario;

public record UsuarioRequest(
        Long empresaId,
        String nome,
        String cpf,
        String email,
        String telefone,
        String senha,
        PerfilUsuario perfil
) {}