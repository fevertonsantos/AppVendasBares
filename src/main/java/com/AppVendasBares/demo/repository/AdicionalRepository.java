package com.AppVendasBares.demo.repository;

import com.AppVendasBares.demo.domain.entity.Adicional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdicionalRepository extends JpaRepository<Adicional, Long> {
    List<Adicional> findByEmpresaIdAndAtivoTrue(Long empresaId);
}
