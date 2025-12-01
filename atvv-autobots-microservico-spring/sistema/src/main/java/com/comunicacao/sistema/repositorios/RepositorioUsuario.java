package com.comunicacao.sistema.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.comunicacao.sistema.entidades.Usuario;

public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);
}