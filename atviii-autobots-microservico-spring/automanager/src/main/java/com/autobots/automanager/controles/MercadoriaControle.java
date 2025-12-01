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

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

	@Autowired
	private RepositorioMercadoria repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Mercadoria>> obterMercadoria(@PathVariable Long id) {
		Mercadoria mercadoria = repositorio.findById(id).orElse(null);
		if (mercadoria == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Mercadoria> modelo = EntityModel.of(mercadoria);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).atualizarMercadoria(mercadoria))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).excluirMercadoria(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias())
				.withRel("todos");
		
		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/mercadorias")
	public ResponseEntity<CollectionModel<EntityModel<Mercadoria>>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		List<EntityModel<Mercadoria>> modelos = mercadorias.stream().map(mercadoria -> {
			EntityModel<Mercadoria> modelo = EntityModel.of(mercadoria);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(mercadoria.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());
		
		CollectionModel<EntityModel<Mercadoria>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias()).withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Mercadoria>> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		Mercadoria novaMercadoria = repositorio.save(mercadoria);
		EntityModel<Mercadoria> modelo = EntityModel.of(novaMercadoria);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(novaMercadoria.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Mercadoria>> atualizarMercadoria(@RequestBody Mercadoria mercadoria) {
		Mercadoria mercadoriaBanco = repositorio.findById(mercadoria.getId()).orElse(null);
		if (mercadoriaBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		mercadoriaBanco.setNome(mercadoria.getNome());
		mercadoriaBanco.setDescricao(mercadoria.getDescricao());
		mercadoriaBanco.setCadastro(mercadoria.getCadastro());
		mercadoriaBanco.setFabricao(mercadoria.getFabricao());
		mercadoriaBanco.setValidade(mercadoria.getValidade());
		mercadoriaBanco.setQuantidade(mercadoria.getQuantidade());
		mercadoriaBanco.setValor(mercadoria.getValor());
		
		repositorio.save(mercadoriaBanco);
		
		EntityModel<Mercadoria> modelo = EntityModel.of(mercadoriaBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadoria(mercadoriaBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable Long id) {
		Mercadoria mercadoria = repositorio.findById(id).orElse(null);
		if (mercadoria == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(mercadoria);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}