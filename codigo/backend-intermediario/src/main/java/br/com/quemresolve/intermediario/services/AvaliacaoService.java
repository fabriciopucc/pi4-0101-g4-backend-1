package br.com.quemresolve.intermediario.services;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.banco.ClienteDados;
import br.com.quemresolve.intermediario.seguranca.Seguranca;
import br.com.quemresolve.intermediario.utilitarios.ErroApi;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.*;

public class AvaliacaoService {

  private final ClienteDados dados;
  private final SessaoService sessoes;

  public AvaliacaoService(ClienteDados dados, SessaoService sessoes) {
    this.dados = dados;
    this.sessoes = sessoes;
  }

  public JsonObject listarAvaliacoes(HttpServerExchange x) {

    String id = parametroConsulta(x.getQueryString(), "id").trim();

    if (id.isBlank()) {

      throw new ErroApi(400, "Identificador do prestador não informado.");
    }

    String userId = "";

    String t = sessoes.obterCookie(x);

    if (!t.isBlank()) {
      JsonObject r = dados.chamar("USER_SESSION", ClienteDados.objeto( "token", Seguranca.calcularSha(t), "now", Long.toString(System.currentTimeMillis())));

      if (ClienteDados.encontrado(r)) {
        userId = s(r.getAsJsonObject("value"), "id");
      }
    }

    JsonObject objeto = dados.chamar("REVIEWS", ClienteDados.objeto("prestador_id", id, "usuario_id", userId));
    JsonObject resposta = new JsonObject();

    resposta.add("avaliacoes", objeto.get("avaliacoes"));
    resposta.add("contatado", objeto.get("contatado"));

    return resposta;
  }

  public JsonObject avaliarPrestador(HttpServerExchange x) throws Exception {
    JsonObject u = sessoes.autenticar(x);

    if (!s(u, "role").equals("cliente")) {

      throw new ErroApi(403, "Use uma conta de cliente para avaliar.");
    }

    JsonObject b = corpo(x);

    String id = s(b, "id_prestador").trim();
    String comentario = s(b, "comentario").trim();

    double valorNota = n(b, "nota", -1);

    if (id.isBlank()) {

      throw new ErroApi(400, "Identificador do prestador não informado.");
    }

    if (!Double.isFinite(valorNota) || valorNota != Math.floor(valorNota) || valorNota < 1 || valorNota > 5) {

      throw new ErroApi(400, "Nota inválida.");
    }

    int nota = (int) valorNota;

    if (!Boolean.parseBoolean(s(b, "confirmado")) || comentario.length() < 10 || comentario.length() > 1000) {

      throw new ErroApi( 400, "Confirme o atendimento, selecione uma nota e escreva pelo menos 10 caracteres.");
    }

    JsonObject d = ClienteDados.objeto("usuario_id", s(u, "id"),"prestador_id", id);
    JsonObject contato = dados.chamar("HAS_CONTACT", d);

    if (!contato.has("exists") || !contato.get("exists").getAsBoolean()) {

      throw new ErroApi(403, "Você precisa iniciar contato antes de avaliar.");
    }

    d.addProperty("nota", nota);
    d.addProperty("comentario", comentario);
    d.addProperty("criado", System.currentTimeMillis());

    dados.chamar("UPSERT_REVIEW", d);

    JsonObject objeto = new JsonObject();
    objeto.addProperty("ok", true);

    return objeto;
  }
}
