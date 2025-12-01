package com.comunicacao.sistema.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.comunicacao.sistema.entidades.Funcionario;
import com.comunicacao.sistema.entidades.Loja;
import com.comunicacao.sistema.repositorios.RepositorioFuncionario;
import com.comunicacao.sistema.repositorios.RepositorioLoja;

@RestController
public class ControleFuncionario {

	@Autowired
	private RepositorioFuncionario repositorioFuncionario;

	@Autowired
	private RepositorioLoja repositorioLoja;

	@GetMapping("/lojas/{idLoja}/funcionarios")
	public ResponseEntity<List<Funcionario>> obterFuncionariosPorLoja(@PathVariable Long idLoja) {
		Loja loja = repositorioLoja.findById(idLoja).orElse(null);
		if (loja == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<Funcionario> funcionarios = repositorioFuncionario.findByLoja(loja);
		return new ResponseEntity<>(funcionarios, HttpStatus.OK);
	}
}