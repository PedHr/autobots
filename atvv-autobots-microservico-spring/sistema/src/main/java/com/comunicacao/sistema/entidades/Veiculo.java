package com.comunicacao.sistema.entidades;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Veiculo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String marca;
	private String modelo;
	private int ano;
	private String placa;

	@ManyToOne
	@JoinColumn(name = "loja_id")
	private Loja loja;

	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;
}