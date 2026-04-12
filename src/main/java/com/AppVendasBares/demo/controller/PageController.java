package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.domain.enums.SetorProducao;
import com.AppVendasBares.demo.dto.PedidoResponse;
import com.AppVendasBares.demo.dto.ProdutoResponse;
import com.AppVendasBares.demo.repository.CategoriaProdutoRepository;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import com.AppVendasBares.demo.service.DashboardService;
import com.AppVendasBares.demo.service.MesaService;
import com.AppVendasBares.demo.service.PedidoService;
import com.AppVendasBares.demo.service.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Arrays;
import java.util.List;

@Controller
public class PageController {

    private final ProdutoService produtoService;
    private final MesaService mesaService;
    private final PedidoService pedidoService;
    private final DashboardService dashboardService;
    private final CategoriaProdutoRepository categoriaRepository;
    private final EmpresaRepository empresaRepository;

    public PageController(ProdutoService produtoService,
                          MesaService mesaService,
                          PedidoService pedidoService,
                          DashboardService dashboardService,
                          CategoriaProdutoRepository categoriaRepository,
                          EmpresaRepository empresaRepository) {
        this.produtoService = produtoService;
        this.mesaService = mesaService;
        this.pedidoService = pedidoService;
        this.dashboardService = dashboardService;
        this.categoriaRepository = categoriaRepository;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "views/login";
    }

    @GetMapping("/registrar")
    public String registrarPage(Model model) {
        model.addAttribute("empresas", empresaRepository.findAll());
        return "views/registrar";
    }

    @GetMapping("/cozinha/{empresaId}")
    public String cozinhaPage(@PathVariable Long empresaId, Model model) {
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("setores", Arrays.stream(SetorProducao.values()).map(Enum::name).toList());
        return "views/cozinha";
    }

    @GetMapping("/admin/dashboard/{empresaId}")
    public String dashboardPage(@PathVariable Long empresaId, Model model) {
        model.addAttribute("empresaId", empresaId);
        model.addAttribute("dashboard", dashboardService.getDashboard(empresaId));
        return "views/dashboard";
    }

    @GetMapping("/pedido/acompanhar/{pedidoId}")
    public String acompanharPedido(@PathVariable Long pedidoId, Model model) {
        model.addAttribute("pedidoId", pedidoId);
        model.addAttribute("pedido", pedidoService.buscarPorId(pedidoId));
        return "views/acompanhar";
    }

    @GetMapping("/comanda/{mesaId}")
    public String comandaPage(@PathVariable Long mesaId, Model model) {
        Mesa mesa = mesaService.buscarPorId(mesaId);
        List<PedidoResponse> pedidos = pedidoService.listarPorMesa(mesaId);
        java.math.BigDecimal grandTotal = pedidos.stream()
                .map(PedidoResponse::total)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
        model.addAttribute("mesa", mesa);
        model.addAttribute("pedidos", pedidos);
        model.addAttribute("grandTotal", grandTotal);
        return "views/comanda";
    }

    @GetMapping("/admin/selecionar-empresa")
    public String selecionarEmpresaPage(Model model) {
        model.addAttribute("empresas", empresaRepository.findAll());
        return "views/selecionar-empresa";
    }
}
