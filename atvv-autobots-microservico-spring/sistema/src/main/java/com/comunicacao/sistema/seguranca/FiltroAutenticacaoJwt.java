package com.comunicacao.sistema.seguranca;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class FiltroAutenticacaoJwt extends OncePerRequestFilter {

	private GerenciadorTokenJwt gerenciadorTokenJwt;
	private ServicoAutenticacao servicoAutenticacao;

	public FiltroAutenticacaoJwt(GerenciadorTokenJwt gerenciadorTokenJwt, ServicoAutenticacao servicoAutenticacao) {
		this.gerenciadorTokenJwt = gerenciadorTokenJwt;
		this.servicoAutenticacao = servicoAutenticacao;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean valido = gerenciadorTokenJwt.isTokenValido(token);
		
		if (valido) {
			autenticarCliente(token, request);
		}
		
		filterChain.doFilter(request, response);
	}

	private void autenticarCliente(String token, HttpServletRequest request) {
		String username = gerenciadorTokenJwt.obterUsernameDoToken(token);
		UserDetails usuario = servicoAutenticacao.loadUserByUsername(username);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}
}