package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.domain.entity.Usuario;
import com.AppVendasBares.demo.dto.LoginRequest;
import com.AppVendasBares.demo.dto.RecuperarSenhaRequest;
import com.AppVendasBares.demo.dto.RegistroClienteRequest;
import com.AppVendasBares.demo.repository.MesaRepository;
import com.AppVendasBares.demo.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final MesaRepository mesaRepository;

    public AuthController(AuthService authService, MesaRepository mesaRepository) {
        this.authService = authService;
        this.mesaRepository = mesaRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpSession session) {
        try {
            Usuario usuario = authService.login(request);
            session.setAttribute("usuarioId", usuario.getId());
            session.setAttribute("empresaId", usuario.getEmpresa().getId());
            session.setAttribute("perfil", usuario.getPerfil().name());
            session.setAttribute("nomeUsuario", usuario.getNome());
            session.setMaxInactiveInterval(20 * 60); // 20 minutes

            // Find first available mesa for this empresa to use as default
            Long defaultMesaId = 1L;
            List<Mesa> mesas = mesaRepository.findByEmpresaId(usuario.getEmpresa().getId());
            if (!mesas.isEmpty()) {
                defaultMesaId = mesas.get(0).getId();
            }
            session.setAttribute("mesaId", defaultMesaId);

            return ResponseEntity.ok(Map.of(
                    "id", usuario.getId(),
                    "nome", usuario.getNome(),
                    "email", usuario.getEmail(),
                    "perfil", usuario.getPerfil().name(),
                    "empresaId", usuario.getEmpresa().getId(),
                    "mesaId", defaultMesaId
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegistroClienteRequest request) {
        try {
            Usuario usuario = authService.registrar(request);
            return ResponseEntity.ok(Map.of(
                    "id", usuario.getId(),
                    "nome", usuario.getNome(),
                    "email", usuario.getEmail(),
                    "mensagem", "Cadastro realizado com sucesso"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/recuperar-senha")
    public ResponseEntity<?> recuperarSenha(@Valid @RequestBody RecuperarSenhaRequest request) {
        try {
            authService.recuperarSenha(request.email());
            return ResponseEntity.ok(Map.of("mensagem", "Senha temporária enviada para o e-mail informado"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(Map.of("mensagem", "Sessão encerrada"));
    }

    @GetMapping("/sessao")
    public ResponseEntity<?> sessao(HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(401).body(Map.of("erro", "Não autenticado"));
        }
        return ResponseEntity.ok(Map.of(
                "usuarioId", usuarioId,
                "empresaId", session.getAttribute("empresaId"),
                "perfil", session.getAttribute("perfil"),
                "nome", session.getAttribute("nomeUsuario")
        ));
    }

    @PostMapping("/selecionar-empresa/{empresaId}")
    public ResponseEntity<?> selecionarEmpresa(@PathVariable Long empresaId, HttpSession session) {
        Long usuarioId = (Long) session.getAttribute("usuarioId");
        if (usuarioId == null) {
            return ResponseEntity.status(401).body(Map.of("erro", "Não autenticado"));
        }
        String perfil = (String) session.getAttribute("perfil");
        if (!"MASTER".equals(perfil)) {
            return ResponseEntity.status(403).body(Map.of("erro", "Acesso negado"));
        }
        session.setAttribute("empresaId", empresaId);
        return ResponseEntity.ok(Map.of("mensagem", "Empresa selecionada", "empresaId", empresaId));
    }
}
