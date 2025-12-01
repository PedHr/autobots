package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.DocumentoControle;
import com.autobots.automanager.entidades.Documento;

@Component
public class AdicionadorLinkDocumento implements AdicionadorLink<Documento> {

	@Override
	public void adicionarLink(List<Documento> lista) {
		for (Documento documento : lista) {
			adicionarLink(documento);
		}
	}

	@Override
	public void adicionarLink(Documento objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.obterDocumento(objeto.getId()))
				.withSelfRel();
		objeto.add(linkProprio);

		Link linkColecao = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.obterDocumentos())
				.withRel("documentos");
		objeto.add(linkColecao);

		Link linkAlterar = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.atualizarDocumento(objeto))
				.withRel("alterar");
		objeto.add(linkAlterar);

		Link linkExcluir = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(DocumentoControle.class)
						.excluirDocumento(objeto.getId()))
				.withRel("excluir");
		objeto.add(linkExcluir);
	}
}