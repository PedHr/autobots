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

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

	@Autowired
	private RepositorioUsuario repositorio;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Usuario>> obterUsuario(@PathVariable Long id) {
		Usuario usuario = repositorio.findById(id).orElse(null);
		if (usuario == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		EntityModel<Usuario> modelo = EntityModel.of(usuario);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(id))
				.withSelfRel();
		Link linkEditar = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).atualizarUsuario(usuario))
				.withRel("editar");
		Link linkExcluir = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).excluirUsuario(id))
				.withRel("excluir");
		Link linkTodos = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios())
				.withRel("todos");

		modelo.add(linkProprio, linkEditar, linkExcluir, linkTodos);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@GetMapping("/usuarios")
	public ResponseEntity<CollectionModel<EntityModel<Usuario>>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		List<EntityModel<Usuario>> modelos = usuarios.stream().map(usuario -> {
			EntityModel<Usuario> modelo = EntityModel.of(usuario);
			Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(usuario.getId()))
					.withSelfRel();
			modelo.add(linkProprio);
			return modelo;
		}).collect(Collectors.toList());

		CollectionModel<EntityModel<Usuario>> colecao = CollectionModel.of(modelos);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios())
				.withSelfRel();
		colecao.add(linkProprio);
		return new ResponseEntity<>(colecao, HttpStatus.OK);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<EntityModel<Usuario>> cadastrarUsuario(@RequestBody Usuario usuario) {
		Usuario novoUsuario = repositorio.save(usuario);
		EntityModel<Usuario> modelo = EntityModel.of(novoUsuario);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(novoUsuario.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.CREATED);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@RequestBody Usuario usuario) {
		Usuario usuarioBanco = repositorio.findById(usuario.getId()).orElse(null);
		if (usuarioBanco == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		// Atualização de dados simples
		usuarioBanco.setNome(usuario.getNome());
		usuarioBanco.setNomeSocial(usuario.getNomeSocial());
		
		// Atualização de agregados (Endereço, Telefones, etc)
		// Aqui optamos por limpar e adicionar os novos para manter a consistência, 
		// ou apenas atualizar se o ID for o mesmo. Para simplificar e garantir a integridade do JSON enviado:
		
		if (usuario.getEndereco() != null) {
			usuarioBanco.setEndereco(usuario.getEndereco());
		}
		
		if (usuario.getTelefones() != null && !usuario.getTelefones().isEmpty()) {
			usuarioBanco.getTelefones().clear();
			usuarioBanco.getTelefones().addAll(usuario.getTelefones());
		}
		
		if (usuario.getDocumentos() != null && !usuario.getDocumentos().isEmpty()) {
			usuarioBanco.getDocumentos().clear();
			usuarioBanco.getDocumentos().addAll(usuario.getDocumentos());
		}
		
		if (usuario.getEmails() != null && !usuario.getEmails().isEmpty()) {
			usuarioBanco.getEmails().clear();
			usuarioBanco.getEmails().addAll(usuario.getEmails());
		}
		
		if (usuario.getCredenciais() != null && !usuario.getCredenciais().isEmpty()) {
			usuarioBanco.getCredenciais().clear();
			usuarioBanco.getCredenciais().addAll(usuario.getCredenciais());
		}
		
		if (usuario.getPerfis() != null && !usuario.getPerfis().isEmpty()) {
			usuarioBanco.getPerfis().clear();
			usuarioBanco.getPerfis().addAll(usuario.getPerfis());
		}

		repositorio.save(usuarioBanco);

		EntityModel<Usuario> modelo = EntityModel.of(usuarioBanco);
		Link linkProprio = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(usuarioBanco.getId()))
				.withSelfRel();
		modelo.add(linkProprio);
		return new ResponseEntity<>(modelo, HttpStatus.OK);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		Usuario usuario = repositorio.findById(id).orElse(null);
		if (usuario == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		repositorio.delete(usuario);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}