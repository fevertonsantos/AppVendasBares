package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.domain.entity.Usuario;
import com.AppVendasBares.demo.domain.enums.PerfilUsuario;
import com.AppVendasBares.demo.dto.LoginRequest;
import com.AppVendasBares.demo.dto.RegistroClienteRequest;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import com.AppVendasBares.demo.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(UsuarioRepository usuarioRepository,
                       EmpresaRepository empresaRepository,
                       PasswordEncoder passwordEncoder,
                       EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public Usuario login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("E-mail ou senha inválidos"));

        if (!usuario.getAtivo()) {
            throw new IllegalArgumentException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.senha(), usuario.getSenhaHash())) {
            throw new IllegalArgumentException("E-mail ou senha inválidos");
        }

        usuario.setUltimoLogin(LocalDateTime.now());
        usuarioRepository.save(usuario);
        return usuario;
    }

    public Usuario registrar(RegistroClienteRequest request) {
        if (usuarioRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }
        if (usuarioRepository.findByCpf(request.cpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }

        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setEmpresa(empresa);
        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        usuario.setTelefone(request.telefone());
        usuario.setSenhaHash(passwordEncoder.encode(request.senha()));
        usuario.setPerfil(PerfilUsuario.CLIENTE);
        usuario.setAtivo(true);

        return usuarioRepository.save(usuario);
    }

    public void recuperarSenha(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("E-mail não encontrado"));

        String senhaTemporaria = gerarSenhaTemporaria();
        usuario.setSenhaHash(passwordEncoder.encode(senhaTemporaria));
        usuarioRepository.save(usuario);

        emailService.enviarSenhaTemporaria(email, usuario.getNome(), senhaTemporaria);
    }

    private String gerarSenhaTemporaria() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
