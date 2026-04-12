package com.AppVendasBares.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AppVendasBares.demo.service.PrintService;

@RestController
public class PrintController {

    private final PrintService printService;

    public PrintController(PrintService printService) {
        this.printService = printService;
    }

    @GetMapping("/api/imprimir-teste")
    public ResponseEntity<String> imprimirTeste() {
        try {
            printService.imprimirTeste();
            return ResponseEntity.ok("Impressão enviada com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro ao imprimir: " + e.getMessage());
        }
    }
}