package com.comunicacao.api.modelos;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Produto {
	private Long id;
	private String nome;
	private String descricao;
	private Double valor;
	private LocalDate dataCadastro;
}