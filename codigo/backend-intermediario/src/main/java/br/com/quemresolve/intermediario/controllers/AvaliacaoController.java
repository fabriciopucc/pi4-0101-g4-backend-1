package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.AvaliacaoService;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.enviar;

public class AvaliacaoController {

  private final AvaliacaoService servico;

  public AvaliacaoController(AvaliacaoService servico) {
    this.servico = servico;
  }

  public void listarAvaliacoes(HttpServerExchange x) {
    enviar(x, 200, servico.listarAvaliacoes(x));
  }

  public void avaliarPrestador(HttpServerExchange x) throws Exception {
    enviar(x, 200, servico.avaliarPrestador(x));
  }
}
