package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.enums.StatusPedido;
import com.AppVendasBares.demo.dto.PedidoItemResponse;
import com.AppVendasBares.demo.dto.PedidoRequest;
import com.AppVendasBares.demo.dto.PedidoResponse;
import com.AppVendasBares.demo.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody PedidoRequest request) {
        try {
            PedidoResponse response = pedidoService.criarPedido(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/empresa/{empresaId}")
    public List<PedidoResponse> listarPorEmpresa(@PathVariable Long empresaId) {
        return pedidoService.listarPorEmpresa(empresaId);
    }

    @GetMapping("/mesa/{mesaId}")
    public List<PedidoResponse> listarPorMesa(@PathVariable Long mesaId) {
        return pedidoService.listarPorMesa(mesaId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoService.buscarPorId(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/historico/{usuarioId}")
    public List<PedidoResponse> historicoCliente(@PathVariable Long usuarioId) {
        return pedidoService.historicoCliente(usuarioId);
    }

    @PutMapping("/item/{itemId}/status")
    public ResponseEntity<?> atualizarStatusItem(@PathVariable Long itemId,
                                                  @RequestParam String status) {
        try {
            StatusPedido novoStatus = StatusPedido.valueOf(status);
            PedidoResponse response = pedidoService.atualizarStatusItem(itemId, novoStatus);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<?> fecharComanda(@PathVariable Long id) {
        try {
            PedidoResponse response = pedidoService.fecharComanda(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @PutMapping("/mesa/{mesaId}/fechar")
    public ResponseEntity<?> fecharComandaPorMesa(@PathVariable Long mesaId) {
        try {
            List<PedidoResponse> responses = pedidoService.fecharComandaPorMesa(mesaId);
            return ResponseEntity.ok(Map.of("mensagem", "Comanda fechada", "pedidos", responses));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping("/fila/{empresaId}/{setor}")
    public List<PedidoItemResponse> listarFila(@PathVariable Long empresaId,
                                                @PathVariable String setor) {
        return pedidoService.listarFilaPorSetor(empresaId, setor);
    }
}
