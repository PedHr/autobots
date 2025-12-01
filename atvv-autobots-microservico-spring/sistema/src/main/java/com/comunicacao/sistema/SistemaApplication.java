package com.comunicacao.sistema;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.comunicacao.sistema.entidades.*;
import com.comunicacao.sistema.repositorios.*;

@SpringBootApplication
public class SistemaApplication implements CommandLineRunner {

	@Autowired private RepositorioLoja repositorioLoja;
	@Autowired private RepositorioCliente repositorioCliente;
	@Autowired private RepositorioFuncionario repositorioFuncionario;
	@Autowired private RepositorioVeiculo repositorioVeiculo;
	@Autowired private RepositorioProduto repositorioProduto;
	@Autowired private RepositorioServico repositorioServico;
	@Autowired private RepositorioVenda repositorioVenda;
	@Autowired private RepositorioUsuario repositorioUsuario;
	@Autowired private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SistemaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Usuario admin = new Usuario();
		admin.setNome("Administrador");
		admin.setUsername("admin");
		admin.setSenha(passwordEncoder.encode("admin123")); 
		repositorioUsuario.save(admin);

		Loja loja1 = new Loja();
		loja1.setNome("Toyota Matriz");
		loja1.setCnpj("12.345.678/0001-90");
		repositorioLoja.save(loja1);
		
		Loja loja2 = new Loja();
		loja2.setNome("Volkswagen Filial");
		loja2.setCnpj("98.765.432/0001-10");
		repositorioLoja.save(loja2);

		Cliente clienteRef = null;
		for (int i = 0; i < 3; i++) {
			Cliente cliente = new Cliente();
			cliente.setNome("Cliente Toyota " + (i + 1));
			cliente.setCpf("111.222.333-0" + i);
			cliente.setTelefone("(11) 99999-000" + i);
			cliente.setEndereco("Rua das Toyotas, " + i);
			cliente.setLoja(loja1);
			clienteRef = repositorioCliente.save(cliente);
		}
		
		Funcionario funcRef = null;
		for (int i = 0; i < 2; i++) {
			Funcionario func = new Funcionario();
			func.setNome("Vendedor Toyota " + i);
			func.setCargo("Vendedor");
			func.setCpf("999.888.777-0" + i);
			func.setLoja(loja1);
			funcRef = repositorioFuncionario.save(func);
		}

		Veiculo carro = new Veiculo();
		carro.setMarca("Toyota");
		carro.setModelo("Corolla");
		carro.setAno(2022);
		carro.setPlaca("ABC-1234");
		carro.setLoja(loja1);
		carro.setCliente(clienteRef);
		repositorioVeiculo.save(carro);
		
		Produto prod = new Produto();
		prod.setNome("Óleo de Motor");
		prod.setDescricao("Óleo sintético 5W30");
		prod.setValor(50.0);
		prod.setDataCadastro(LocalDate.now());
		prod.setLoja(loja1);
		repositorioProduto.save(prod);
		
		Servico serv = new Servico();
		serv.setNome("Troca de Óleo");
		serv.setDescricao("Mão de obra para troca completa");
		serv.setValor(100.0);
		serv.setDataCadastro(LocalDate.now());
		serv.setLoja(loja1);
		repositorioServico.save(serv);
		
		Venda venda = new Venda();
		venda.setData(LocalDate.now());
		venda.setLoja(loja1);
		venda.setCliente(clienteRef);
		venda.setFuncionario(funcRef);
		venda.setProdutos(Arrays.asList(prod));
		venda.setServicos(Arrays.asList(serv));
		venda.setValorTotal(150.0);
		repositorioVenda.save(venda);
	}
}