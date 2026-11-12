package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.PrestadorService;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.enviar;

public class PrestadorController {

  private final PrestadorService servico;

  public PrestadorController(PrestadorService servico) {
    this.servico = servico;
  }

  public void listar(HttpServerExchange x) {
    enviar(x, 200, servico.listarPrestadores());
  }

  public void contatarPrestador(HttpServerExchange x) throws Exception {
    enviar(x, 200, servico.contatarPrestador(x));
  }
}
