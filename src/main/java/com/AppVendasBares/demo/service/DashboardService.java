package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.Pedido;
import com.AppVendasBares.demo.domain.enums.StatusMesa;
import com.AppVendasBares.demo.dto.DashboardResponse;
import com.AppVendasBares.demo.dto.ProdutoVendidoResponse;
import com.AppVendasBares.demo.repository.MesaRepository;
import com.AppVendasBares.demo.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
public class DashboardService {

    private final PedidoRepository pedidoRepository;
    private final MesaRepository mesaRepository;

    public DashboardService(PedidoRepository pedidoRepository, MesaRepository mesaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.mesaRepository = mesaRepository;
    }

    public DashboardResponse getDashboard(Long empresaId) {
        LocalDate hoje = LocalDate.now();
        LocalDateTime inicioDia = hoje.atStartOfDay();
        LocalDateTime fimDia = hoje.atTime(LocalTime.MAX);
        LocalDateTime inicioSemana = hoje.with(java.time.DayOfWeek.MONDAY).atStartOfDay();
        LocalDateTime inicioMes = hoje.with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();

        BigDecimal faturamentoDiario = calcularFaturamento(empresaId, inicioDia, fimDia);
        BigDecimal faturamentoSemanal = calcularFaturamento(empresaId, inicioSemana, fimDia);
        BigDecimal faturamentoMensal = calcularFaturamento(empresaId, inicioMes, fimDia);

        int totalPedidosHoje = pedidoRepository.countByEmpresaIdAndCriadoEmAfter(empresaId, inicioDia);
        BigDecimal ticketMedio = totalPedidosHoje > 0
                ? faturamentoDiario.divide(BigDecimal.valueOf(totalPedidosHoje), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        long mesasOcupadas = mesaRepository.findByEmpresaId(empresaId).stream()
                .filter(m -> m.getStatus() == StatusMesa.OCUPADA)
                .count();
        long mesasLivres = mesaRepository.findByEmpresaId(empresaId).stream()
                .filter(m -> m.getStatus() == StatusMesa.LIVRE)
                .count();

        List<Object[]> topProdutos = pedidoRepository.findProdutosMaisVendidos(empresaId, inicioMes, fimDia);
        List<ProdutoVendidoResponse> produtosMaisVendidos = topProdutos.stream()
                .limit(10)
                .map(row -> new ProdutoVendidoResponse(null, (String) row[0], ((Number) row[1]).longValue()))
                .toList();

        return new DashboardResponse(
                faturamentoDiario,
                faturamentoSemanal,
                faturamentoMensal,
                ticketMedio,
                totalPedidosHoje,
                (int) mesasOcupadas,
                (int) mesasLivres,
                produtosMaisVendidos
        );
    }

    private BigDecimal calcularFaturamento(Long empresaId, LocalDateTime start, LocalDateTime end) {
        List<Pedido> pedidos = pedidoRepository.findByEmpresaIdAndCriadoEmBetween(empresaId, start, end);
        return pedidos.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
