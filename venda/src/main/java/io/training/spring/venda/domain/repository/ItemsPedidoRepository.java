package io.training.spring.venda.domain.repository;

import io.training.spring.venda.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemsPedidoRepository extends JpaRepository<ItemPedido, Long> {
}
