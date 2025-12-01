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

import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping("/venda")
public class VendaControle {

	@Autowired
	private RepositorioVenda repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Venda>> obterVenda(@PathVariable Long id) {
		Venda venda = repositorio.findById(id).orElse(null);
		if (venda == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Venda> modelo = EntityModel.of(venda);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).atualizarVenda(venda))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).excluirVenda(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas())
				.withRel("todos");
		
		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/vendas")
	public ResponseEntity<CollectionModel<EntityModel<Venda>>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		List<EntityModel<Venda>> modelos = vendas.stream().map(venda -> {
			EntityModel<Venda> modelo = EntityModel.of(venda);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(venda.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());
		
		CollectionModel<EntityModel<Venda>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas()).withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Venda>> cadastrarVenda(@RequestBody Venda venda) {
		Venda novaVenda = repositorio.save(venda);
		EntityModel<Venda> modelo = EntityModel.of(novaVenda);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(novaVenda.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Venda>> atualizarVenda(@RequestBody Venda venda) {
		Venda vendaBanco = repositorio.findById(venda.getId()).orElse(null);
		if (vendaBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		vendaBanco.setCadastro(venda.getCadastro());
		vendaBanco.setIdentificacao(venda.getIdentificacao());
		vendaBanco.setCliente(venda.getCliente());
		vendaBanco.setFuncionario(venda.getFuncionario());
		vendaBanco.setVeiculo(venda.getVeiculo());
		
		if(venda.getMercadorias() != null) {
			vendaBanco.getMercadorias().clear();
			vendaBanco.getMercadorias().addAll(venda.getMercadorias());
		}
		
		if(venda.getServicos() != null) {
			vendaBanco.getServicos().clear();
			vendaBanco.getServicos().addAll(venda.getServicos());
		}

		repositorio.save(vendaBanco);
		
		EntityModel<Venda> modelo = EntityModel.of(vendaBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(vendaBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
		Venda venda = repositorio.findById(id).orElse(null);
		if (venda == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(venda);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}