package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.Pedido;
import com.AppVendasBares.demo.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByEmpresaIdOrderByCriadoEmDesc(Long empresaId);
    List<Pedido> findByMesaIdOrderByCriadoEmDesc(Long mesaId);
    List<Pedido> findByUsuarioIdAndCriadoEmAfterOrderByCriadoEmDesc(Long usuarioId, LocalDateTime after);
    List<Pedido> findByEmpresaIdAndCriadoEmBetween(Long empresaId, LocalDateTime start, LocalDateTime end);
    int countByEmpresaIdAndCriadoEmAfter(Long empresaId, LocalDateTime after);

    @Query("SELECT pi.produto.nome, SUM(pi.quantidade) as total FROM PedidoItem pi " +
           "WHERE pi.pedido.empresa.id = :empresaId " +
           "AND pi.pedido.criadoEm BETWEEN :start AND :end " +
           "GROUP BY pi.produto.id, pi.produto.nome ORDER BY total DESC")
    List<Object[]> findProdutosMaisVendidos(@Param("empresaId") Long empresaId,
                                            @Param("start") LocalDateTime start,
                                            @Param("end") LocalDateTime end);
}
