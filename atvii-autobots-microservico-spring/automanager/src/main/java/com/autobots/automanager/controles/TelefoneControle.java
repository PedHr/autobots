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

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
public class TelefoneControle {
	@Autowired
	private TelefoneRepositorio repositorio;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLink;

	@GetMapping("/telefones")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = repositorio.findAll();
		if (telefones.isEmpty()) {
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(telefones);
			ResponseEntity<List<Telefone>> resposta = new ResponseEntity<>(telefones, HttpStatus.OK);
			return resposta;
		}
	}

	@GetMapping("/telefone/{id}")
	public ResponseEntity<Telefone> obterTelefone(@PathVariable long id) {
		Optional<Telefone> telefone = repositorio.findById(id);
		if (telefone.isPresent()) {
			Telefone telefoneSelecionado = telefone.get();
			adicionadorLink.adicionarLink(telefoneSelecionado);
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(telefoneSelecionado, HttpStatus.OK);
			return resposta;
		} else {
			ResponseEntity<Telefone> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		}
	}

	@PostMapping("/telefone")
	public ResponseEntity<?> cadastrarTelefone(@RequestBody Telefone telefone) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (telefone.getId() == null) {
			repositorio.save(telefone);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/telefone")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Telefone> telefoneOptional = repositorio.findById(atualizacao.getId());
		if (telefoneOptional.isPresent()) {
			Telefone telefone = telefoneOptional.get();
			TelefoneAtualizador atualizador = new TelefoneAtualizador();
			atualizador.atualizar(telefone, atualizacao);
			repositorio.save(telefone);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/telefone/{id}")
	public ResponseEntity<?> excluirTelefone(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Telefone> telefone = repositorio.findById(id);
		if (telefone.isPresent()) {
			repositorio.delete(telefone.get());
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}