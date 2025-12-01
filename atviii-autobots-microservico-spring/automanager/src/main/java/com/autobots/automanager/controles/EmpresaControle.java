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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

	@Autowired
	private RepositorioEmpresa repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Empresa>> obterEmpresa(@PathVariable Long id) {
		Empresa empresa = repositorio.findById(id).orElse(null);
		if (empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Empresa> modelo = EntityModel.of(empresa);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).atualizarEmpresa(empresa))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).excluirEmpresa(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas())
				.withRel("todos");
		
		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/empresas")
	public ResponseEntity<CollectionModel<EntityModel<Empresa>>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		List<EntityModel<Empresa>> modelos = empresas.stream().map(empresa -> {
			EntityModel<Empresa> modelo = EntityModel.of(empresa);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(empresa.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());
		
		CollectionModel<EntityModel<Empresa>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas()).withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Empresa>> cadastrarEmpresa(@RequestBody Empresa empresa) {
		Empresa novaEmpresa = repositorio.save(empresa);
		EntityModel<Empresa> modelo = EntityModel.of(novaEmpresa);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(novaEmpresa.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Empresa>> atualizarEmpresa(@RequestBody Empresa empresa) {
		Empresa empresaBanco = repositorio.findById(empresa.getId()).orElse(null);
		if (empresaBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		empresaBanco.setNomeFantasia(empresa.getNomeFantasia());
		empresaBanco.setRazaoSocial(empresa.getRazaoSocial());
		empresaBanco.setEndereco(empresa.getEndereco());
		empresaBanco.setTelefones(empresa.getTelefones());
		empresaBanco.setCadastro(empresa.getCadastro());
		
		repositorio.save(empresaBanco);
		
		EntityModel<Empresa> modelo = EntityModel.of(empresaBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(empresaBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable Long id) {
		Empresa empresa = repositorio.findById(id).orElse(null);
		if (empresa == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(empresa);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}