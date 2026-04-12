package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.CategoriaProduto;
import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.repository.CategoriaProdutoRepository;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaProdutoRepository categoriaRepository;
    private final EmpresaRepository empresaRepository;

    public CategoriaController(CategoriaProdutoRepository categoriaRepository,
                               EmpresaRepository empresaRepository) {
        this.categoriaRepository = categoriaRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/empresa/{empresaId}")
    public List<CategoriaProduto> listarPorEmpresa(@PathVariable Long empresaId) {
        return categoriaRepository.findByEmpresaIdOrderByOrdemAsc(empresaId);
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Map<String, Object> body) {
        Long empresaId = ((Number) body.get("empresaId")).longValue();
        String nome = (String) body.get("nome");
        Integer ordem = body.get("ordem") != null ? ((Number) body.get("ordem")).intValue() : 0;

        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        CategoriaProduto categoria = new CategoriaProduto();
        categoria.setEmpresa(empresa);
        categoria.setNome(nome);
        categoria.setOrdem(ordem);
        categoria.setAtivo(true);

        return ResponseEntity.ok(categoriaRepository.save(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    if (body.containsKey("nome")) categoria.setNome((String) body.get("nome"));
                    if (body.containsKey("ordem")) categoria.setOrdem(((Number) body.get("ordem")).intValue());
                    if (body.containsKey("ativo")) categoria.setAtivo((Boolean) body.get("ativo"));
                    return ResponseEntity.ok(categoriaRepository.save(categoria));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(categoria -> {
                    categoria.setAtivo(false);
                    categoriaRepository.save(categoria);
                    return ResponseEntity.ok(Map.of("mensagem", "Categoria desativada"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
