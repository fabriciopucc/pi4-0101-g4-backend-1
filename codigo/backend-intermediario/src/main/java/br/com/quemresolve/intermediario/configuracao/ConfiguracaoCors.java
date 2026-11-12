package br.com.quemresolve.intermediario.configuracao;

import io.undertow.server.*;
import io.undertow.util.*;

import java.util.*;
import java.util.stream.Collectors;

public class ConfiguracaoCors {

  private final Set<String> origins;

  public ConfiguracaoCors(String origins) {
    this.origins = Arrays.stream(origins.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .collect(Collectors.toSet());
  }

  public boolean permite(String origem) {
    return origins.contains(origem);
  }

  // CORS

  public void aplicarCors(HttpServerExchange x) {

    String origem = x.getRequestHeaders()
            .getFirst(Headers.ORIGIN);

    x.getResponseHeaders().put(
            Headers.VARY,
            "Origin"
    );

    if (origem != null && origins.contains(origem)) {

      x.getResponseHeaders().put(
              new HttpString("Access-Control-Allow-Origin"),
              origem
      );

      x.getResponseHeaders().put(
              new HttpString("Access-Control-Allow-Credentials"),
              "true"
      );
    }

    x.getResponseHeaders().put(
            new HttpString("Access-Control-Allow-Methods"),
            "GET, POST, OPTIONS"
    );

    x.getResponseHeaders().put(
            new HttpString("Access-Control-Allow-Headers"),
            "Content-Type"
    );
  }
}
