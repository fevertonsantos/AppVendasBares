package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Usuario;
import com.AppVendasBares.demo.dto.UsuarioRequest;
import com.AppVendasBares.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public Usuario criar(@RequestBody UsuarioRequest request) {
        return usuarioService.criar(request);
    }
}