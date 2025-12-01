package com.comunicacao.api.servicos;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.comunicacao.api.modelos.DadosLogin;
import com.comunicacao.api.modelos.Loja;
import com.comunicacao.api.modelos.Produto;
import com.comunicacao.api.modelos.TokenDto;

@Service
public class ClienteSistema {

	private final String URL_BASE = "http://localhost:8080";
	private final RestTemplate restTemplate = new RestTemplate();

	public TokenDto realizarLogin(String username, String senha) {
		DadosLogin login = new DadosLogin(username, senha);
		ResponseEntity<TokenDto> resposta = restTemplate.postForEntity(URL_BASE + "/login", login, TokenDto.class);
		return resposta.getBody();
	}

	public Loja[] listarLojas(String token) {
		HttpHeaders headers = criarHeaders(token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<Loja[]> resposta = restTemplate.exchange(
				URL_BASE + "/lojas", 
				HttpMethod.GET, 
				entity, 
				Loja[].class);
		
		return resposta.getBody();
	}
	
	public Produto[] listarProdutosDaLoja(String token, Long idLoja) {
		HttpHeaders headers = criarHeaders(token);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		
		ResponseEntity<Produto[]> resposta = restTemplate.exchange(
				URL_BASE + "/lojas/" + idLoja + "/produtos", 
				HttpMethod.GET, 
				entity, 
				Produto[].class);
		
		return resposta.getBody();
	}

	private HttpHeaders criarHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		return headers;
	}
}