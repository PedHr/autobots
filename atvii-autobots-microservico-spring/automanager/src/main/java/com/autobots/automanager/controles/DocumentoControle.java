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

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
public class DocumentoControle {
	@Autowired
	private DocumentoRepositorio repositorio;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLink;

	@GetMapping("/documentos")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = repositorio.findAll();
		if (documentos.isEmpty()) {
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		} else {
			adicionadorLink.adicionarLink(documentos);
			ResponseEntity<List<Documento>> resposta = new ResponseEntity<>(documentos, HttpStatus.OK);
			return resposta;
		}
	}

	@GetMapping("/documento/{id}")
	public ResponseEntity<Documento> obterDocumento(@PathVariable long id) {
		Optional<Documento> documento = repositorio.findById(id);
		if (documento.isPresent()) {
			Documento documentoSelecionado = documento.get();
			adicionadorLink.adicionarLink(documentoSelecionado);
			ResponseEntity<Documento> resposta = new ResponseEntity<>(documentoSelecionado, HttpStatus.OK);
			return resposta;
		} else {
			ResponseEntity<Documento> resposta = new ResponseEntity<>(HttpStatus.NOT_FOUND);
			return resposta;
		}
	}

	@PostMapping("/documento")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody Documento documento) {
		HttpStatus status = HttpStatus.CONFLICT;
		if (documento.getId() == null) {
			repositorio.save(documento);
			status = HttpStatus.CREATED;
		}
		return new ResponseEntity<>(status);
	}

	@PutMapping("/documento")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento atualizacao) {
		HttpStatus status = HttpStatus.CONFLICT;
		Optional<Documento> documentoOptional = repositorio.findById(atualizacao.getId());
		if (documentoOptional.isPresent()) {
			Documento documento = documentoOptional.get();
			DocumentoAtualizador atualizador = new DocumentoAtualizador();
			atualizador.atualizar(documento, atualizacao);
			repositorio.save(documento);
			status = HttpStatus.OK;
		} else {
			status = HttpStatus.BAD_REQUEST;
		}
		return new ResponseEntity<>(status);
	}

	@DeleteMapping("/documento/{id}")
	public ResponseEntity<?> excluirDocumento(@PathVariable long id) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		Optional<Documento> documento = repositorio.findById(id);
		if (documento.isPresent()) {
			repositorio.delete(documento.get());
			status = HttpStatus.OK;
		}
		return new ResponseEntity<>(status);
	}
}