package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.dto.ProdutoRequest;
import com.AppVendasBares.demo.dto.ProdutoResponse;
import com.AppVendasBares.demo.service.ProdutoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @PostMapping
    public ProdutoResponse criar(@RequestBody ProdutoRequest request) {
        return produtoService.criar(request);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<ProdutoResponse> listarPorEmpresa(@PathVariable Long empresaId) {
        return produtoService.listarPorEmpresa(empresaId);
    }
}