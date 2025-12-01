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

import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

	@Autowired
	private RepositorioVeiculo repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Veiculo>> obterVeiculo(@PathVariable Long id) {
		Veiculo veiculo = repositorio.findById(id).orElse(null);
		if (veiculo == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Veiculo> modelo = EntityModel.of(veiculo);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).atualizarVeiculo(veiculo))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).excluirVeiculo(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos())
				.withRel("todos");
		
		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/veiculos")
	public ResponseEntity<CollectionModel<EntityModel<Veiculo>>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		List<EntityModel<Veiculo>> modelos = veiculos.stream().map(veiculo -> {
			EntityModel<Veiculo> modelo = EntityModel.of(veiculo);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(veiculo.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());
		
		CollectionModel<EntityModel<Veiculo>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos()).withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Veiculo>> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		Veiculo novoVeiculo = repositorio.save(veiculo);
		EntityModel<Veiculo> modelo = EntityModel.of(novoVeiculo);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(novoVeiculo.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Veiculo>> atualizarVeiculo(@RequestBody Veiculo veiculo) {
		Veiculo veiculoBanco = repositorio.findById(veiculo.getId()).orElse(null);
		if (veiculoBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		veiculoBanco.setModelo(veiculo.getModelo());
		veiculoBanco.setPlaca(veiculo.getPlaca());
		veiculoBanco.setTipo(veiculo.getTipo());
		veiculoBanco.setProprietario(veiculo.getProprietario());
		
		repositorio.save(veiculoBanco);
		
		EntityModel<Veiculo> modelo = EntityModel.of(veiculoBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculo(veiculoBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
		Veiculo veiculo = repositorio.findById(id).orElse(null);
		if (veiculo == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(veiculo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}