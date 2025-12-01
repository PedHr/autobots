package com.comunicacao.sistema.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Cliente;
import com.comunicacao.sistema.entidades.Loja;
import com.comunicacao.sistema.repositorios.RepositorioCliente;
import com.comunicacao.sistema.repositorios.RepositorioLoja;

@RestController
public class ControleLoja {

	@Autowired
	private RepositorioLoja repositorioLoja;

	@Autowired
	private RepositorioCliente repositorioCliente;

	@GetMapping("/lojas/{idLoja}/clientes")
	public ResponseEntity<List<Cliente>> obterClientesPorLoja(@PathVariable Long idLoja) {
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Cliente> clientes = repositorioCliente.findByLoja(loja);
		return new ResponseEntity<>(clientes, HttpStatus.OK);
	}
	
	@GetMapping("/lojas")
	public ResponseEntity<List<Loja>> obterLojas() {
		List<Loja> lojas = repositorioLoja.findAll();
		return new ResponseEntity<>(lojas, HttpStatus.OK);
	}
}