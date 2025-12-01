package com.comunicacao.sistema.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Produto;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioProduto extends JpaRepository<Produto, Long> {
	List<Produto> findByLoja(Loja loja);
}