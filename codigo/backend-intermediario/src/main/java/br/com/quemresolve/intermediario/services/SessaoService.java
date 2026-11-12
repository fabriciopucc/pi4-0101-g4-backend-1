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

public class SessaoService {

  private final ClienteDados dados;
  private final boolean cookieSeguro;

  public SessaoService(ClienteDados dados, boolean cookieSeguro) {
    this.dados = dados;
    this.cookieSeguro = cookieSeguro;
  }

  public JsonObject autenticar(HttpServerExchange x) {

    String t = obterCookie(x);

    if (t.isBlank()) {
      throw new ErroApi(
              401,
              "Entre na sua conta para continuar."
      );
    }

    JsonObject r = dados.chamar(
            "USER_SESSION",
            ClienteDados.objeto(
                    "token", Seguranca.calcularSha(t),
                    "now", Long.toString(System.currentTimeMillis())
            )
    );

    if (!ClienteDados.encontrado(r)) {
      throw new ErroApi(
              401,
              "Entre na sua conta para continuar."
      );
    }

    return r.getAsJsonObject("value");
  }

  public void abrirSessao(HttpServerExchange x, JsonObject u) {

    String raw = Seguranca.gerarToken();

    dados.chamar(
            "SAVE_SESSION",
            ClienteDados.objeto(
                    "token", Seguranca.calcularSha(raw),
                    "usuario_Id", s(u, "id"),
                    "expira", Long.toString(
                            System.currentTimeMillis() + 604800000L
                    )
            )
    );

    x.getResponseHeaders().put(
            Headers.SET_COOKIE,
            cabecalhoCookie(raw, 604800)
    );
  }

  public String cabecalhoCookie(String value, long maxAge) {

    String sameSite = cookieSeguro
            ? "SameSite=None; Secure"
            : "SameSite=Lax";

    return "qr_session=" + value +
            "; HttpOnly" +
            "; " + sameSite +
            "; Path=/" +
            "; Max-Age=" + maxAge;
  }

  public String obterCookie(HttpServerExchange x) {

    String c = x.getRequestHeaders()
            .getFirst(Headers.COOKIE);

    if (c == null) {
      return "";
    }

    for (String p : c.split(";")) {

      String[] a = p.trim().split("=", 2);

      if (a.length == 2 &&
              a[0].equals("qr_session")) {

        return a[1];
      }
    }

    return "";
  }
}
