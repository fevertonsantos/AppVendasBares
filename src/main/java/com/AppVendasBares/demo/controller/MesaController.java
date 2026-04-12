package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.dto.MesaRequest;
import com.AppVendasBares.demo.service.MesaService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MesaController {

    private final MesaService mesaService;
    private final SimpMessagingTemplate messagingTemplate;

    public MesaController(MesaService mesaService, SimpMessagingTemplate messagingTemplate) {
        this.mesaService = mesaService;
        this.messagingTemplate = messagingTemplate;
    }

    @PostMapping("/mesas")
    public Mesa criar(@RequestBody MesaRequest request) {
        return mesaService.criar(request);
    }

    @GetMapping("/mesas/empresa/{empresaId}")
    public List<Mesa> listarPorEmpresa(@PathVariable Long empresaId) {
        return mesaService.listarPorEmpresa(empresaId);
    }

    @PostMapping("/mesa/chamar-garcom")
    public ResponseEntity<?> chamarGarcom(@RequestBody Map<String, Object> body) {
        Object empresaIdObj = body.get("empresaId");
        Long empresaId = empresaIdObj != null ? Long.valueOf(empresaIdObj.toString()) : 1L;
        messagingTemplate.convertAndSend("/topic/garcom/" + empresaId,
                Map.of("tipo", "CHAMAR_GARCOM", "mensagem", "Cliente solicitou um garcom!"));
        return ResponseEntity.ok(Map.of("mensagem", "Garcom chamado com sucesso"));
    }
}
