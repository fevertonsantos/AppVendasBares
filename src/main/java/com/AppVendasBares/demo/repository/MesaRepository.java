package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    List<Mesa> findByEmpresaId(Long empresaId);
}