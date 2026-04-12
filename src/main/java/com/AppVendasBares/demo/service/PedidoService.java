package com.AppVendasBares.demo.service;

import com.AppVendasBares.demo.domain.entity.*;
import com.AppVendasBares.demo.domain.enums.StatusMesa;
import com.AppVendasBares.demo.domain.enums.StatusPedido;
import com.AppVendasBares.demo.dto.*;
import com.AppVendasBares.demo.repository.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoItemRepository pedidoItemRepository;
    private final ProdutoRepository produtoRepository;
    private final AdicionalRepository adicionalRepository;
    private final MesaRepository mesaRepository;
    private final EmpresaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PedidoService(PedidoRepository pedidoRepository,
                         PedidoItemRepository pedidoItemRepository,
                         ProdutoRepository produtoRepository,
                         AdicionalRepository adicionalRepository,
                         MesaRepository mesaRepository,
                         EmpresaRepository empresaRepository,
                         UsuarioRepository usuarioRepository,
                         SimpMessagingTemplate messagingTemplate) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoItemRepository = pedidoItemRepository;
        this.produtoRepository = produtoRepository;
        this.adicionalRepository = adicionalRepository;
        this.mesaRepository = mesaRepository;
        this.empresaRepository = empresaRepository;
        this.usuarioRepository = usuarioRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    public PedidoResponse criarPedido(PedidoRequest request) {
        Empresa empresa = empresaRepository.findById(request.empresaId())
                .orElseThrow(() -> new IllegalArgumentException("Empresa não encontrada"));

        Mesa mesa = mesaRepository.findById(request.mesaId())
                .orElseThrow(() -> new IllegalArgumentException("Mesa não encontrada"));

        Pedido pedido = new Pedido();
        pedido.setEmpresa(empresa);
        pedido.setMesa(mesa);

        if (request.usuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(request.usuarioId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
            pedido.setUsuario(usuario);
        }

        if (request.garcomId() != null) {
            Usuario garcom = usuarioRepository.findById(request.garcomId())
                    .orElseThrow(() -> new IllegalArgumentException("Garçom não encontrado"));
            pedido.setGarcom(garcom);
        }

        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setCriadoEm(LocalDateTime.now());

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (PedidoItemRequest itemReq : request.itens()) {
            Produto produto = produtoRepository.findById(itemReq.produtoId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemReq.produtoId()));

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemReq.quantidade());
            item.setObservacao(itemReq.observacao());
            item.setSetor(produto.getSetorProducao());
            item.setStatus(StatusPedido.RECEBIDO);

            BigDecimal subtotalItem = produto.getPreco().multiply(BigDecimal.valueOf(itemReq.quantidade()));

            if (itemReq.adicionais() != null) {
                for (AdicionalItemRequest adicReq : itemReq.adicionais()) {
                    Adicional adicional = adicionalRepository.findById(adicReq.adicionalId())
                            .orElseThrow(() -> new IllegalArgumentException("Adicional não encontrado"));

                    int qtdCobrada = Math.max(0, adicReq.quantidade() - adicional.getQtdInclusa());
                    BigDecimal precoAdicional = adicional.getPreco();
                    BigDecimal subtotalAdicional = precoAdicional.multiply(BigDecimal.valueOf(qtdCobrada));

                    PedidoItemAdicional pia = new PedidoItemAdicional();
                    pia.setPedidoItem(item);
                    pia.setAdicional(adicional);
                    pia.setQuantidade(adicReq.quantidade());
                    pia.setPrecoUnitario(precoAdicional);
                    pia.setSubtotal(subtotalAdicional);

                    item.getAdicionais().add(pia);
                    subtotalItem = subtotalItem.add(subtotalAdicional);
                }
            }

            item.setSubtotal(subtotalItem);
            pedido.getItens().add(item);
            totalPedido = totalPedido.add(subtotalItem);
        }

        pedido.setTotal(totalPedido);

        // Mark mesa as occupied
        mesa.setStatus(StatusMesa.OCUPADA);
        mesaRepository.save(mesa);

        Pedido salvo = pedidoRepository.save(pedido);

        PedidoResponse response = toResponse(salvo);

        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/pedidos/" + empresa.getId(), response);
        for (PedidoItem item : salvo.getItens()) {
            messagingTemplate.convertAndSend(
                    "/topic/fila/" + empresa.getId() + "/" + item.getSetor().name(),
                    toItemResponse(item)
            );
        }

        return response;
    }

    @Transactional
    public PedidoResponse atualizarStatusItem(Long itemId, StatusPedido novoStatus) {
        PedidoItem item = pedidoItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Item não encontrado"));

        item.setStatus(novoStatus);
        pedidoItemRepository.save(item);

        Pedido pedido = item.getPedido();
        atualizarStatusPedido(pedido);

        PedidoResponse response = toResponse(pedido);
        Long empresaId = pedido.getEmpresa().getId();

        messagingTemplate.convertAndSend("/topic/pedidos/" + empresaId, response);
        messagingTemplate.convertAndSend(
                "/topic/fila/" + empresaId + "/" + item.getSetor().name(),
                toItemResponse(item)
        );
        messagingTemplate.convertAndSend(
                "/topic/pedido-status/" + pedido.getId(),
                response
        );

        return response;
    }

    @Transactional
    public PedidoResponse fecharComanda(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));

        pedido.setStatus(StatusPedido.FECHADO);
        pedidoRepository.save(pedido);

        // Check if mesa has other open orders, if not free it
        List<Pedido> pedidosMesa = pedidoRepository.findByMesaIdOrderByCriadoEmDesc(pedido.getMesa().getId());
        boolean todosEncerrados = pedidosMesa.stream()
                .allMatch(p -> p.getStatus() == StatusPedido.FECHADO || p.getStatus() == StatusPedido.CANCELADO);
        if (todosEncerrados) {
            Mesa mesa = pedido.getMesa();
            mesa.setStatus(StatusMesa.LIVRE);
            mesaRepository.save(mesa);
        }

        PedidoResponse response = toResponse(pedido);
        messagingTemplate.convertAndSend("/topic/pedidos/" + pedido.getEmpresa().getId(), response);

        return response;
    }

    public List<PedidoResponse> listarPorEmpresa(Long empresaId) {
        return pedidoRepository.findByEmpresaIdOrderByCriadoEmDesc(empresaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PedidoResponse> listarPorMesa(Long mesaId) {
        return pedidoRepository.findByMesaIdOrderByCriadoEmDesc(mesaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PedidoResponse buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado"));
        return toResponse(pedido);
    }

    public List<PedidoResponse> historicoCliente(Long usuarioId) {
        LocalDateTime seisAtras = LocalDateTime.now().minusMonths(6);
        return pedidoRepository.findByUsuarioIdAndCriadoEmAfterOrderByCriadoEmDesc(usuarioId, seisAtras)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PedidoItemResponse> listarFilaPorSetor(Long empresaId, String setor) {
        List<PedidoItem> itens = pedidoItemRepository.findByPedidoEmpresaIdAndSetorAndStatusIn(
                empresaId,
                com.AppVendasBares.demo.domain.enums.SetorProducao.valueOf(setor),
                List.of(StatusPedido.RECEBIDO, StatusPedido.EM_FILA, StatusPedido.EM_PREPARO)
        );
        return itens.stream().map(this::toItemResponse).toList();
    }

    private void atualizarStatusPedido(Pedido pedido) {
        List<PedidoItem> itens = pedido.getItens();
        if (itens.isEmpty()) return;

        boolean todosProntos = itens.stream().allMatch(i -> i.getStatus() == StatusPedido.PRONTO);
        boolean todosEntregues = itens.stream().allMatch(i -> i.getStatus() == StatusPedido.ENTREGUE);
        boolean algumEmPreparo = itens.stream().anyMatch(i -> i.getStatus() == StatusPedido.EM_PREPARO);
        boolean algumEmFila = itens.stream().anyMatch(i -> i.getStatus() == StatusPedido.EM_FILA);

        if (todosEntregues) {
            pedido.setStatus(StatusPedido.ENTREGUE);
        } else if (todosProntos) {
            pedido.setStatus(StatusPedido.PRONTO);
        } else if (algumEmPreparo) {
            pedido.setStatus(StatusPedido.EM_PREPARO);
        } else if (algumEmFila) {
            pedido.setStatus(StatusPedido.EM_FILA);
        }

        pedidoRepository.save(pedido);
    }

    private PedidoResponse toResponse(Pedido pedido) {
        List<PedidoItemResponse> itensResp = pedido.getItens().stream()
                .map(this::toItemResponse)
                .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getMesa().getId(),
                pedido.getMesa().getNumero(),
                pedido.getUsuario() != null ? pedido.getUsuario().getNome() : null,
                pedido.getGarcom() != null ? pedido.getGarcom().getNome() : null,
                pedido.getStatus().name(),
                pedido.getTotal(),
                pedido.getTempoEstimado(),
                pedido.getCriadoEm(),
                itensResp
        );
    }

    private PedidoItemResponse toItemResponse(PedidoItem item) {
        List<AdicionalItemResponse> adicionaisResp = item.getAdicionais().stream()
                .map(pia -> new AdicionalItemResponse(
                        pia.getAdicional().getNome(),
                        pia.getQuantidade(),
                        pia.getPrecoUnitario(),
                        pia.getSubtotal()
                ))
                .toList();

        return new PedidoItemResponse(
                item.getId(),
                item.getProduto().getNome(),
                item.getQuantidade(),
                item.getObservacao(),
                item.getSetor().name(),
                item.getStatus().name(),
                item.getSubtotal(),
                adicionaisResp
        );
    }
}
