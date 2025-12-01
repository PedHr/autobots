package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.modelos.AdicionadorLinkCliente;
import com.autobots.automanager.modelos.ClienteAtualizador;
import com.autobots.automanager.modelos.ClienteSelecionador;
import com.autobots.automanager.repositorios.ClienteRepositorio;

@RestController
public class ClienteControle {
	@Autowired
	private ClienteRepositorio repositorio;
	@Autowired
	private ClienteSelecionador selecionador;
	@Autowired
	private AdicionadorLinkCliente adicionadorLink;

	@GetMapping("/cliente/{id}")
	public ResponseEntity<Cliente> obterCliente(@PathVariable long id) {
		List<Cliente> clientes = repositorio.findAll();
		Cliente cliente = selecionador.selecionar(clientes, id);
		if (cliente == null) {
			ResponseEntity<Cliente> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(cliente);
			ResponseEntity<Cliente> resposta = new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
			return resposta;
		}
	}

	@GetMapping("/clientes")
	public ResponseEntity<List<Cliente>> obterClientes() {
		List<Cliente> clientes = repositorio.findAll();
		if (clientes.isEmpty()) {
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(clientes);
			ResponseEntity<List<Cliente>> resposta = new ResponseEntity<>(clientes, HttpStatus.OK);
			return resposta;
		}
	}

	@PostMapping("/cliente")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Cliente cliente) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (cliente.getId() == null) {
			repositorio.save(cliente);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/cliente")
	public ResponseEntity<?> atualizarCliente(@RequestBody Cliente atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Cliente> clienteOptional = repositorio.findById(atualizacao.getId());
		if (clienteOptional.isPresent()) {
			Cliente cliente = clienteOptional.get();
			ClienteAtualizador atualizador = new ClienteAtualizador();
			atualizador.atualizar(cliente, atualizacao);
			repositorio.save(cliente);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/cliente/{id}")
	public ResponseEntity<?> excluirCliente(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Cliente> clienteOptional = repositorio.findById(id);
		if (clienteOptional.isPresent()) {
			repositorio.delete(clienteOptional.get());
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}