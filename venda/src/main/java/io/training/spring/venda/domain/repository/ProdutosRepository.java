package io.training.spring.venda.domain.repository;

import io.training.spring.venda.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutosRepository extends JpaRepository<Produto, Long> {
}
