package com.comunicacao.sistema.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Funcionario;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioFuncionario extends JpaRepository<Funcionario, Long> {
	List<Funcionario> findByLoja(Loja loja);
}