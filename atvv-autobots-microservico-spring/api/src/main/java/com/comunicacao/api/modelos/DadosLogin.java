package com.comunicacao.api.modelos;

import lombok.Data;

@Data
public class DadosLogin {
	private String username;
	private String senha;
	
	public DadosLogin(String username, String senha) {
		this.username = username;
		this.senha = senha;
	}
}