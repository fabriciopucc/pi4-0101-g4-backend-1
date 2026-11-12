package br.com.quemresolve.intermediario.services;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import br.com.quemresolve.intermediario.banco.ClienteDados;
import br.com.quemresolve.intermediario.utilitarios.ErroApi;
import static br.com.quemresolve.intermediario.utilitarios.UtilJson.*;

public class PrestadorService {

  private final ClienteDados dados;
  private final SessaoService sessoes;

  public PrestadorService(ClienteDados dados, SessaoService sessoes) {
    this.dados = dados;
    this.sessoes = sessoes;
  }

  public JsonObject listarPrestadores() {

    JsonObject resposta = dados.chamar("PROVIDERS", new JsonObject());

    JsonObject objeto = new JsonObject();
    objeto.add("prestadores", resposta.get("prestadores"));

    return objeto;
  }

  public JsonObject contatarPrestador(HttpServerExchange x) throws Exception {

    JsonObject u = sessoes.autenticar(x);

    if (!s(u, "role").equals("cliente")) {

      throw new ErroApi(403, "Use uma conta de cliente para solicitar atendimento.");
    }

    String id = s(corpo(x), "id").trim();

    if (id.isBlank()) {

      throw new ErroApi(400, "Identificador do prestador não informado.");
    }

    System.out.println(id);

    JsonObject p = dados.chamar("PHONE", ClienteDados.objeto("usuario_id", id));

    System.out.println(p);

    if (!ClienteDados.encontrado(p)) {

      throw new ErroApi(404, "Prestador não encontrado.");
    }

    dados.chamar("CONTACT", ClienteDados.objeto("usuario_id", s(u, "id"), "prestador_id", id));

    String phone = s(p.getAsJsonObject("value"), "phone");

    String msg = URLEncoder.encode("Olá! Encontrei seu perfil no Quem Resolve e gostaria de um orçamento.", StandardCharsets.UTF_8);

    JsonObject objeto = new JsonObject();
    objeto.addProperty("url", "https://wa.me/" + phone + "?text=" + msg);

    return objeto;
  }
}
