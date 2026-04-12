package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.CategoriaProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaProdutoRepository extends JpaRepository<CategoriaProduto, Long> {
    List<CategoriaProduto> findByEmpresaIdOrderByOrdemAsc(Long empresaId);
}