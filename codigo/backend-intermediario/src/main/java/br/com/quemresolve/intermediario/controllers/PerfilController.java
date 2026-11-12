package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.PerfilService;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.enviar;

public class PerfilController {

  private final PerfilService servico;

  public PerfilController(PerfilService servico) {
    this.servico = servico;
  }

  public void salvarPerfil(HttpServerExchange x) throws Exception {
    enviar(x, 200, servico.salvarPerfil(x));
  }
}
