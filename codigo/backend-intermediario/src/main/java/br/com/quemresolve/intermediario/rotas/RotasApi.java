package br.com.quemresolve.intermediario.rotas;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.configuracao.ConfiguracaoCors;
import br.com.quemresolve.intermediario.controllers.*;
import br.com.quemresolve.intermediario.banco.ClienteDados;
import br.com.quemresolve.intermediario.services.*;
import br.com.quemresolve.intermediario.utilitarios.ErroApi;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.*;

public class RotasApi implements HttpHandler {

  private final ConfiguracaoCors controleCors;
  private final AutenticacaoController autenticacaoController;
  private final PerfilController perfilController;
  private final PrestadorController prestadorController;
  private final AvaliacaoController avaliacaoController;

  public RotasApi(ClienteDados dados, ConfiguracaoCors controleCors, boolean cookieSeguro) {
    this.controleCors = controleCors;

    SessaoService sessoes = new SessaoService(dados, cookieSeguro);

    this.autenticacaoController = new AutenticacaoController(new AutenticacaoService(dados, sessoes));
    this.perfilController = new PerfilController(new PerfilService(dados, sessoes));
    this.prestadorController = new PrestadorController(new PrestadorService(dados, sessoes));
    this.avaliacaoController = new AvaliacaoController(new AvaliacaoService(dados, sessoes));
  }

  @Override
  public void handleRequest(HttpServerExchange x) {
    controleCors.aplicarCors(x);

    // Requisições OPTIONS (preflight do CORS)
    if (Methods.OPTIONS.equals(x.getRequestMethod())) {
      x.setStatusCode(204);
      x.endExchange();
      return;
    }

    // Transfere o processamento para uma Worker Thread
    x.dispatch(() -> {

      try {
        x.startBlocking();

        String origem = x.getRequestHeaders() .getFirst(Headers.ORIGIN);

        if (origem != null && !controleCors.permite(origem)) {
          enviar(x, 403, erro("Origem inválida."));
          return;
        }

        String caminho = x.getRequestPath();
        String metodo = x.getRequestMethod().toString();

        if (metodo.equals("POST") && caminho.equals("/api/cadastro")) {
          autenticacaoController.cadastrar(x);

        } else if (metodo.equals("POST") && caminho.equals("/api/entrar")) {
          autenticacaoController.entrar(x);

        } else if (metodo.equals("GET") && caminho.equals("/api/usuario-atual")) {
          autenticacaoController.usuarioAtual(x);

        } else if (metodo.equals("POST") && caminho.equals("/api/sair")) {
          autenticacaoController.sair(x);

        } else if (metodo.equals("GET") && caminho.equals("/api/prestadores")) {
          prestadorController.listar(x);

        } else if (metodo.equals("POST") && caminho.equals("/api/perfil")) {
          perfilController.salvarPerfil(x);

        } else if (metodo.equals("GET") && caminho.equals("/api/avaliacoes")) {
          avaliacaoController.listarAvaliacoes(x);

        } else if (metodo.equals("POST") && caminho.equals("/api/contato")) {
          prestadorController.contatarPrestador(x);

        } else if (metodo.equals("POST") && caminho.equals("/api/avaliacao")) {
          avaliacaoController.avaliarPrestador(x);

        } else {
          enviar(x, 404, erro("Operação não encontrada."));
        }
      } catch (ErroApi e) {

        enviar(x, e.status, erro(e.getMessage()));
      } catch (Exception e) {

        System.err.println("Erro API: " + e.getMessage());
        e.printStackTrace();

        enviar(x, 500, erro(
                "Não foi possível concluir. Tente novamente."
        ));
      }
    });
  }
}
