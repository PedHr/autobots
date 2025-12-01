package com.comunicacao.sistema.repositorios;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Venda;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioVenda extends JpaRepository<Venda, Long> {
	List<Venda> findByLojaAndDataBetween(Loja loja, LocalDate inicio, LocalDate fim);
}