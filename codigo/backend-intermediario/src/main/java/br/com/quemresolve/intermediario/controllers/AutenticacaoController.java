package br.com.quemresolve.intermediario.controllers;

import io.undertow.server.HttpServerExchange;

import br.com.quemresolve.intermediario.services.AutenticacaoService;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.enviar;

public class AutenticacaoController {

  private final AutenticacaoService servico;

  public AutenticacaoController(AutenticacaoService servico) {
    this.servico = servico;
  }

  public void cadastrar(HttpServerExchange x) throws Exception {
    enviar(x, 200, servico.cadastrar(x));
  }

  public void entrar(HttpServerExchange x) throws Exception {
    enviar(x, 200, servico.entrar(x));
  }

  public void usuarioAtual(HttpServerExchange x) {
    enviar(x, 200, servico.usuarioAtual(x));
  }

  public void sair(HttpServerExchange x) {
    enviar(x, 200, servico.sair(x));
  }
}
