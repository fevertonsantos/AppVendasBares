package com.AppVendasBares.demo.controller;

import com.AppVendasBares.demo.dto.DashboardResponse;
import com.AppVendasBares.demo.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<DashboardResponse> getDashboard(@PathVariable Long empresaId) {
        return ResponseEntity.ok(dashboardService.getDashboard(empresaId));
    }
}
