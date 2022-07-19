package io.training.spring.venda.api.service;

import io.training.spring.venda.api.dto.PedidoDTO;
import io.training.spring.venda.domain.entity.Pedido;
import io.training.spring.venda.api.enums.StatusPedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO pedidoDTO);

    Optional<Pedido> obterPedidoCompleto(Integer id);
    void atualizarStatus(Long id, StatusPedido statusPedido);
}
