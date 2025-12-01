package com.autobots.automanager.controles;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

	@Autowired
	private RepositorioServico repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Servico>> obterServico(@PathVariable Long id) {
		Servico servico = repositorio.findById(id).orElse(null);
		if (servico == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Servico> modelo = EntityModel.of(servico);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).atualizarServico(servico))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).excluirServico(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos())
				.withRel("todos");
		
		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/servicos")
	public ResponseEntity<CollectionModel<EntityModel<Servico>>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		List<EntityModel<Servico>> modelos = servicos.stream().map(servico -> {
			EntityModel<Servico> modelo = EntityModel.of(servico);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(servico.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());
		
		CollectionModel<EntityModel<Servico>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos()).withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Servico>> cadastrarServico(@RequestBody Servico servico) {
		Servico novoServico = repositorio.save(servico);
		EntityModel<Servico> modelo = EntityModel.of(novoServico);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(novoServico.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Servico>> atualizarServico(@RequestBody Servico servico) {
		Servico servicoBanco = repositorio.findById(servico.getId()).orElse(null);
		if (servicoBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		servicoBanco.setNome(servico.getNome());
		servicoBanco.setDescricao(servico.getDescricao());
		servicoBanco.setValor(servico.getValor());
		
		repositorio.save(servicoBanco);
		
		EntityModel<Servico> modelo = EntityModel.of(servicoBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(servicoBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirServico(@PathVariable Long id) {
		Servico servico = repositorio.findById(id).orElse(null);
		if (servico == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(servico);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}