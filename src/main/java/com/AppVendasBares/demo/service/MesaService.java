package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.domain.entity.Mesa;
import com.AppVendasBares.demo.dto.MesaRequest;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import com.AppVendasBares.demo.repository.MesaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MesaService {

    private final MesaRepository mesaRepository;
    private final EmpresaRepository empresaRepository;

    public MesaService(MesaRepository mesaRepository, EmpresaRepository empresaRepository) {
        this.mesaRepository = mesaRepository;
        this.empresaRepository = empresaRepository;
    }

    public Mesa criar(MesaRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Mesa mesa = new Mesa();
        mesa.setEmpresa(empresa);
        mesa.setNumero(request.numero());
        mesa.setNome(request.nome());
        mesa.setArea(request.area());
        mesa.setSetor(request.setor());
        mesa.setAndar(request.andar());
        mesa.setTipoMesa(request.tipoMesa());
        mesa.setQtdCadeiras(request.qtdCadeiras());
        mesa.setPosicaoX(request.posicaoX());
        mesa.setPosicaoY(request.posicaoY());

        return mesaRepository.save(mesa);
    }

    public List<Mesa> listarPorEmpresa(Long empresaId) {
        return mesaRepository.findByEmpresaId(empresaId);
    }

    public Mesa buscarPorId(Long id) {
        return mesaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Mesa não encontrada"));
    }
}