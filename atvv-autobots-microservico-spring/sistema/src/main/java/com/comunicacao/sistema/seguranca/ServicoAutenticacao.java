package com.comunicacao.sistema.seguranca;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.comunicacao.sistema.entidades.Usuario;
import com.comunicacao.sistema.repositorios.RepositorioUsuario;

@Service
public class ServicoAutenticacao implements UserDetailsService {
	
	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repositorioUsuario.findByUsername(username);
		if (usuario.isPresent()) {
			return org.springframework.security.core.userdetails.User
					.builder()
					.username(usuario.get().getUsername())
					.password(usuario.get().getSenha())
					.roles("USER")
					.build();
		}
		throw new UsernameNotFoundException("Dados inválidos!");
	}
}