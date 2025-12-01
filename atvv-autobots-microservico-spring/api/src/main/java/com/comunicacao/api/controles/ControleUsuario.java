package com.comunicacao.api.controles;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.Usuario;

@RestController
public class ControleUsuario {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@GetMapping("/todos-usuarios")
	public ResponseEntity<?> obterUsuarios() {
		List<Usuario> usuarios = new ArrayList<>();

		ResponseEntity<? extends List> resposta = new RestTemplate()
				.getForEntity("http://localhost:8080/usuarios", usuarios.getClass());
		usuarios = resposta.getBody();
		
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.FOUND);
	}
}