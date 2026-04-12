package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    List<Produto> findByEmpresaIdAndAtivoTrue(Long empresaId);
    List<Produto> findByCategoriaIdAndAtivoTrue(Long categoriaId);
}