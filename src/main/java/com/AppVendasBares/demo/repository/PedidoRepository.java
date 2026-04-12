package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEmpresaIdOrderByCriadoEmDesc(Long empresaId);
    List<Pedido> findByMesaIdOrderByCriadoEmDesc(Long mesaId);
}