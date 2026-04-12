package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.PedidoItem;
import com.AppVendasBares.demo.domain.enums.SetorProducao;
import com.AppVendasBares.demo.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
    List<PedidoItem> findByPedidoId(Long pedidoId);
    List<PedidoItem> findByPedidoEmpresaIdAndSetorAndStatusIn(Long empresaId, SetorProducao setor, List<StatusPedido> statuses);
}
