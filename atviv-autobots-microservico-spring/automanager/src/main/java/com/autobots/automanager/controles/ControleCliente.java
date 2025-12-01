package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.repositorios.RepositorioCliente;

@RestController
public class ControleCliente {
	@Autowired
	private RepositorioCliente repositorio;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR')")
	@PostMapping("/cadastrar-cliente")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		repositorio.save(cliente);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_GERENTE','ROLE_VENDEDOR')")
	@GetMapping("/obter-clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		return new ResponseEntity<>(repositorio.findAll(), HttpStatus.FOUND);
	}
}