package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.dto.ProdutoResponse;
import com.AppVendasBares.demo.service.MesaService;
import com.AppVendasBares.demo.service.ProdutoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cardapio")
public class CardapioController {

    private final ProdutoService produtoService;
    private final MesaService mesaService;

    public CardapioController(ProdutoService produtoService, MesaService mesaService) {
        this.produtoService = produtoService;
        this.mesaService = mesaService;
    }

    @GetMapping("/{mesaId}")
    public String showCardapio(@PathVariable Long mesaId, Model model) {
        Mesa mesa = mesaService.buscarPorId(mesaId);
        Long empresaId = mesa.getEmpresa().getId();

        List<ProdutoResponse> produtos = produtoService.listarPorEmpresa(empresaId);

        model.addAttribute("mesa", mesa);
        model.addAttribute("itensCardapio", produtos);

        return "views/cardapio";
    }

    @PostMapping("/pedido/adicionar")
    @ResponseBody
    public String addItem(@RequestParam Long produtoId, @RequestParam Long mesaId) {
        return "OK";
    }
}