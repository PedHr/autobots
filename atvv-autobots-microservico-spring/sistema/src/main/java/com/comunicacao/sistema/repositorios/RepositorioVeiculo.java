package com.comunicacao.sistema.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Veiculo;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioVeiculo extends JpaRepository<Veiculo, Long> {
	List<Veiculo> findByLoja(Loja loja);
}