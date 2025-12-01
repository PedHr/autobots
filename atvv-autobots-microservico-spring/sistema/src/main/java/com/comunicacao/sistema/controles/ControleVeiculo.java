package com.comunicacao.sistema.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Loja;
import com.comunicacao.sistema.entidades.Veiculo;
import com.comunicacao.sistema.repositorios.RepositorioLoja;
import com.comunicacao.sistema.repositorios.RepositorioVeiculo;

@RestController
public class ControleVeiculo {

	@Autowired
	private RepositorioVeiculo repositorioVeiculo;

	@Autowired
	private RepositorioLoja repositorioLoja;

	@GetMapping("/lojas/{idLoja}/veiculos")
	public ResponseEntity<List<Veiculo>> obterVeiculosPorLoja(@PathVariable Long idLoja) {
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Veiculo> veiculos = repositorioVeiculo.findByLoja(loja);
		return new ResponseEntity<>(veiculos, HttpStatus.OK);
	}
}