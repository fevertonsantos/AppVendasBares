package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.domain.entity.Usuario;
import com.AppVendasBares.demo.dto.UsuarioRequest;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import com.AppVendasBares.demo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            EmpresaRepository empresaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario criar(UsuarioRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);
        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(request.perfil());
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }
}