package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.CategoriaProduto;
import com.AppVendasBares.demo.domain.entity.Empresa;
import com.AppVendasBares.demo.domain.entity.Produto;
import com.AppVendasBares.demo.dto.ProdutoRequest;
import com.AppVendasBares.demo.dto.ProdutoResponse;
import com.AppVendasBares.demo.repository.CategoriaProdutoRepository;
import com.AppVendasBares.demo.repository.EmpresaRepository;
import com.AppVendasBares.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EmpresaRepository empresaRepository;
    private final CategoriaProdutoRepository categoriaRepository;

    public ProdutoService(
            ProdutoRepository produtoRepository,
            EmpresaRepository empresaRepository,
            CategoriaProdutoRepository categoriaRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.empresaRepository = empresaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        CategoriaProduto categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        Produto produto = new Produto();
        produto.setEmpresa(empresa);
        produto.setCategoria(categoria);
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setPreco(request.preco());
        produto.setFoto(request.foto());
        produto.setSetorProducao(request.setorProducao());
        produto.setEstoque(request.estoque() == null ? 0 : request.estoque());
        produto.setAtivo(true);

        Produto salvo = produtoRepository.save(produto);
        return toResponse(salvo);
    }

    public List<ProdutoResponse> listarPorEmpresa(Long empresaId) {
        return produtoRepository.findByEmpresaIdAndAtivoTrue(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProdutoResponse toResponse(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getCategoria().getNome(),
                produto.getSetorProducao().name(),
                produto.getEstoque(),
                produto.getAtivo()
        );
    }
}