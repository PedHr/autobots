package com.comunicacao.sistema.controles;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Loja;
import com.comunicacao.sistema.entidades.Venda;
import com.comunicacao.sistema.repositorios.RepositorioLoja;
import com.comunicacao.sistema.repositorios.RepositorioVenda;

@RestController
public class ControleVenda {

	@Autowired private RepositorioLoja repositorioLoja;
	@Autowired private RepositorioVenda repositorioVenda;

	@GetMapping("/lojas/{idLoja}/vendas")
	public ResponseEntity<List<Venda>> obterVendasPorPeriodo(
			@PathVariable Long idLoja,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
		
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		List<Venda> vendas = repositorioVenda.findByLojaAndDataBetween(loja, inicio, fim);
		return new ResponseEntity<>(vendas, HttpStatus.OK);
	}
}