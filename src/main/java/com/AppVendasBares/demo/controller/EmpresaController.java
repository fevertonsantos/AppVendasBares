package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaRepository empresaRepository;

    public EmpresaController(EmpresaRepository empresaRepository) {
        this.empresaRepository = empresaRepository;
    }

    @GetMapping
    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        return empresaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Empresa empresa) {
        if (empresaRepository.findByCnpj(empresa.getCnpj()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "CNPJ já cadastrado"));
        }
        return ResponseEntity.ok(empresaRepository.save(empresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Empresa dados) {
        return empresaRepository.findById(id)
                .map(empresa -> {
                    empresa.setNomeFantasia(dados.getNomeFantasia());
                    empresa.setRazaoSocial(dados.getRazaoSocial());
                    empresa.setCnpj(dados.getCnpj());
                    empresa.setLogo(dados.getLogo());
                    empresa.setStatus(dados.getStatus());
                    return ResponseEntity.ok(empresaRepository.save(empresa));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
