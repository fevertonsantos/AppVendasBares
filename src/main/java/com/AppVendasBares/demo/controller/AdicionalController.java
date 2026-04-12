package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Adicional;
import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.repository.AdicionalRepository;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/adicionais")
public class AdicionalController {

    private final AdicionalRepository adicionalRepository;
    private final EmpresaRepository empresaRepository;

    public AdicionalController(AdicionalRepository adicionalRepository,
                               EmpresaRepository empresaRepository) {
        this.adicionalRepository = adicionalRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Adicional> listarPorEmpresa(@PathVariable Long empresaId) {
        return adicionalRepository.findByEmpresaIdAndAtivoTrue(empresaId);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        Long empresaId = ((Number) body.get("empresaId")).longValue();
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Adicional adicional = new Adicional();
        adicional.setEmpresa(empresa);
        adicional.setNome((String) body.get("nome"));
        adicional.setPreco(new BigDecimal(body.get("preco").toString()));
        adicional.setQtdInclusa(body.get("qtdInclusa") != null ? ((Number) body.get("qtdInclusa")).intValue() : 0);
        adicional.setQtdMaxima(body.get("qtdMaxima") != null ? ((Number) body.get("qtdMaxima")).intValue() : 1);
        adicional.setAtivo(true);

        return ResponseEntity.ok(adicionalRepository.save(adicional));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return adicionalRepository.findById(id)
                .map(adicional -> {
                    if (body.containsKey("nome")) adicional.setNome((String) body.get("nome"));
                    if (body.containsKey("preco")) adicional.setPreco(new BigDecimal(body.get("preco").toString()));
                    if (body.containsKey("qtdInclusa")) adicional.setQtdInclusa(((Number) body.get("qtdInclusa")).intValue());
                    if (body.containsKey("qtdMaxima")) adicional.setQtdMaxima(((Number) body.get("qtdMaxima")).intValue());
                    if (body.containsKey("ativo")) adicional.setAtivo((Boolean) body.get("ativo"));
                    return ResponseEntity.ok(adicionalRepository.save(adicional));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
