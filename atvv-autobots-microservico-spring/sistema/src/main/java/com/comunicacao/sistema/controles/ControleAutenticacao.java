package com.comunicacao.sistema.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Usuario;
import com.comunicacao.sistema.seguranca.GerenciadorTokenJwt;

import lombok.Data;

@RestController
public class ControleAutenticacao {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private GerenciadorTokenJwt gerenciadorTokenJwt;

	@PostMapping("/login")
	public ResponseEntity<?> autenticar(@RequestBody DadosLogin dadosLogin) {
		UsernamePasswordAuthenticationToken dadosAutenticacao = new UsernamePasswordAuthenticationToken(dadosLogin.getUsername(), dadosLogin.getSenha());
		
		try {
			Authentication authentication = authenticationManager.authenticate(dadosAutenticacao);
			String token = gerenciadorTokenJwt.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Data
	static class DadosLogin {
		private String username;
		private String senha;
	}
	
	@Data
	static class TokenDto {
		private String token;
		private String tipo;
		
		public TokenDto(String token, String tipo) {
			this.token = token;
			this.tipo = tipo;
		}
	}
}