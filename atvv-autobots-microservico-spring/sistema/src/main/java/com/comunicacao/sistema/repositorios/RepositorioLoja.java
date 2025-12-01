package com.comunicacao.sistema.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Loja;

public interface RepositorioLoja extends JpaRepository<Loja, Long> {
}