package io.training.spring.venda.api.service.impl;

import io.training.spring.venda.api.dto.ItemPedidoDTO;
import io.training.spring.venda.api.dto.PedidoDTO;
import io.training.spring.venda.api.service.PedidoService;
import io.training.spring.venda.domain.entity.Cliente;
import io.training.spring.venda.domain.entity.ItemPedido;
import io.training.spring.venda.domain.entity.Pedido;
import io.training.spring.venda.domain.entity.Produto;
import io.training.spring.venda.api.enums.StatusPedido;
import io.training.spring.venda.domain.repository.ClienteRepository;
import io.training.spring.venda.domain.repository.ItemsPedidoRepository;
import io.training.spring.venda.domain.repository.PedidosRepository;
import io.training.spring.venda.domain.repository.ProdutosRepository;
import io.training.spring.venda.exception.PedidoNaoEncontradoException;
import io.training.spring.venda.exception.RegraNegocioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidosRepository pedidosRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutosRepository produtosRepository;
    private final ItemsPedidoRepository itemsPedidoRepository;

    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {

        Long idCliente = dto.getCliente();
        Cliente cliente = clienteRepository.findById(idCliente).orElseThrow(() ->
            new RegraNegocioException("Código de cliente inválido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemsPedido = converterItems(pedido, dto.getItems());
        pedidosRepository.save(pedido);
        itemsPedidoRepository.saveAll(itemsPedido);
        pedido.setItens(itemsPedido);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidosRepository.findByIdFetchItens(Long.valueOf(id));
    }

    @Override
    @Transactional
    public void atualizarStatus(Long id, StatusPedido statusPedido) {
        pedidosRepository.findById(id).map(pedido -> {pedido.setStatus(statusPedido);
            return pedidosRepository.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItems(Pedido pedido, List<ItemPedidoDTO> items) {
        if(items.isEmpty()) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem items.");
        }

        return items.stream().map(dto -> {
            Long idProduto = Long.valueOf(dto.getProduto());
            Produto produto = produtosRepository.findById(idProduto).orElseThrow(() ->
                    new RegraNegocioException("Código de produto inválido: " + idProduto));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(dto.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(produto);
            return itemPedido;
        }).collect(Collectors.toList());
    }
}
