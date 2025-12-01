package com.comunicacao.sistema.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Loja;
import com.comunicacao.sistema.entidades.Produto;
import com.comunicacao.sistema.entidades.Servico;
import com.comunicacao.sistema.repositorios.RepositorioLoja;
import com.comunicacao.sistema.repositorios.RepositorioProduto;
import com.comunicacao.sistema.repositorios.RepositorioServico;

@RestController
public class ControleCatalogo {

	@Autowired private RepositorioLoja repositorioLoja;
	@Autowired private RepositorioProduto repositorioProduto;
	@Autowired private RepositorioServico repositorioServico;

	@GetMapping("/lojas/{idLoja}/produtos")
	public ResponseEntity<List<Produto>> obterProdutos(@PathVariable Long idLoja) {
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(repositorioProduto.findByLoja(loja), HttpStatus.OK);
	}

	@GetMapping("/lojas/{idLoja}/servicos")
	public ResponseEntity<List<Servico>> obterServicos(@PathVariable Long idLoja) {
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(repositorioServico.findByLoja(loja), HttpStatus.OK);
	}
}