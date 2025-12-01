package com.comunicacao.api.controles;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import com.comunicacao.api.modelos.Loja;
import com.comunicacao.api.modelos.Produto;
import com.comunicacao.api.modelos.TokenDto;
import com.comunicacao.api.servicos.ClienteSistema;

@RestController
public class ControleIntegracao {

	@Autowired
	private ClienteSistema clienteSistema;

	@GetMapping("/teste-integracao")
	public ResponseEntity<?> testarIntegracao(
			@RequestParam String user, 
			@RequestParam String pass) {
		
		Map<String, Object> resultado = new HashMap<>();
		
		try {
			TokenDto tokenDto = clienteSistema.realizarLogin(user, pass);
			resultado.put("1. Autenticação", "Sucesso. Token recebido: " + tokenDto.getToken().substring(0, 15) + "...");
			
			Loja[] lojas = clienteSistema.listarLojas(tokenDto.getToken());
			resultado.put("2. Listagem de Lojas", lojas);
			
			if (lojas != null && lojas.length > 0) {
				Long idPrimeiraLoja = lojas[0].getId();
				Produto[] produtos = clienteSistema.listarProdutosDaLoja(tokenDto.getToken(), idPrimeiraLoja);
				resultado.put("3. Produtos da Loja " + lojas[0].getNome(), produtos);
			}
			
			return new ResponseEntity<>(resultado, HttpStatus.OK);

		} catch (HttpClientErrorException e) {
			return new ResponseEntity<>("Erro na integração: " + e.getMessage(), e.getStatusCode());
		} catch (Exception e) {
			return new ResponseEntity<>("Erro interno: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}