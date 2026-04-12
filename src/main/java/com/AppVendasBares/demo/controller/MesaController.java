package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.dto.MesaRequest;
import com.AppVendasBares.demo.service.MesaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private final MesaService mesaService;

    public MesaController(MesaService mesaService) {
        this.mesaService = mesaService;
    }

    @PostMapping
    public Mesa criar(@RequestBody MesaRequest request) {
        return mesaService.criar(request);
    }

    @GetMapping("/empresa/{empresaId}")
    public List<Mesa> listarPorEmpresa(@PathVariable Long empresaId) {
        return mesaService.listarPorEmpresa(empresaId);
    }
}