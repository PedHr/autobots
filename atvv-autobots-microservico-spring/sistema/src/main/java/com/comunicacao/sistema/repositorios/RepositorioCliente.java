package com.comunicacao.sistema.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Cliente;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioCliente extends JpaRepository<Cliente, Long> {
	List<Cliente> findByLoja(Loja loja);
}