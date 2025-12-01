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

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
public class EnderecoControle {
	@Autowired
	private EnderecoRepositorio repositorio;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLink;

	@GetMapping("/enderecos")
	public ResponseEntity<List<Endereco>> obterEnderecos() {
		List<Endereco> enderecos = repositorio.findAll();
		if (enderecos.isEmpty()) {
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(enderecos);
			ResponseEntity<List<Endereco>> resposta = new ResponseEntity<>(enderecos, HttpStatus.OK);
			return resposta;
		}
	}

	@GetMapping("/endereco/{id}")
	public ResponseEntity<Endereco> obterEndereco(@PathVariable long id) {
		Optional<Endereco> endereco = repositorio.findById(id);
		if (endereco.isPresent()) {
			Endereco enderecoSelecionado = endereco.get();
			adicionadorLink.adicionarLink(enderecoSelecionado);
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(enderecoSelecionado, HttpStatus.OK);
			return resposta;
		} else {
			ResponseEntity<Endereco> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		}
	}

	@PostMapping("/endereco")
	public ResponseEntity<?> cadastrarEndereco(@RequestBody Endereco endereco) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (endereco.getId() == null) {
			repositorio.save(endereco);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/endereco")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Endereco> enderecoOptional = repositorio.findById(atualizacao.getId());
		if (enderecoOptional.isPresent()) {
			Endereco endereco = enderecoOptional.get();
			EnderecoAtualizador atualizador = new EnderecoAtualizador();
			atualizador.atualizar(endereco, atualizacao);
			repositorio.save(endereco);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/endereco/{id}")
	public ResponseEntity<?> excluirEndereco(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Endereco> endereco = repositorio.findById(id);
		if (endereco.isPresent()) {
			repositorio.delete(endereco.get());
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}