package com.autobots.automanager.controles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Credencial;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;

@RestController
public class ControleUsuario {

	@Autowired
	private RepositorioUsuario repositorio;
	
	@Autowired
	private ProvedorJwt provedorJwt;

	@PostMapping("/cadastrar-usuario")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		Authentication autenticacao = SecurityContextHolder.getContext().getAuthentication();
		boolean isQuemCadastraAdmin = autenticacao.getAuthorities().stream()
				.anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

		if (usuario.getPerfis().contains(Perfil.ROLE_ADMIN) && !isQuemCadastraAdmin) {
			return new ResponseEntity<>("Apenas administradores podem criar outros administradores.", HttpStatus.FORBIDDEN);
		}

		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		try {
			Credencial credencial = new Credencial();
			credencial.setNomeUsuario(usuario.getCredencial().getNomeUsuario());
			String senha = codificador.encode(usuario.getCredencial().getSenha());
			credencial.setSenha(senha);
			usuario.setCredencial(credencial);
			
			repositorio.save(usuario);
			
			String jwt = provedorJwt.proverJwt(usuario.getCredencial().getNomeUsuario());
			Map<String, String> resposta = new HashMap<>();
			resposta.put("Authorization", "Bearer " + jwt);
			
			return new ResponseEntity<>(resposta, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/obter-usuarios")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE')")
	public ResponseEntity<List<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.FOUND);
	}

	@GetMapping("/usuario/me")
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_GERENTE', 'ROLE_VENDEDOR', 'ROLE_CLIENTE')")
	public ResponseEntity<?> obterDadosProprios(HttpServletRequest request) {
		String cabecalho = request.getHeader("Authorization");
		String jwt = cabecalho.split(" ")[1];
		
		String nomeUsuario = provedorJwt.obterNomeUsuario(jwt);
		
		List<Usuario> usuarios = repositorio.findAll();
		Usuario usuarioLogado = usuarios.stream()
				.filter(u -> u.getCredencial().getNomeUsuario().equals(nomeUsuario))
				.findFirst()
				.orElse(null);

		if (usuarioLogado != null) {
			return new ResponseEntity<>(usuarioLogado, HttpStatus.FOUND);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}