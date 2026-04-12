package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.PedidoItemAdicional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemAdicionalRepository extends JpaRepository<PedidoItemAdicional, Long> {
    List<PedidoItemAdicional> findByPedidoItemId(Long pedidoItemId);
}
