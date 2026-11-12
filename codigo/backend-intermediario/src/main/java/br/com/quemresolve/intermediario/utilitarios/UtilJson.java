package br.com.quemresolve.intermediario.utilitarios;

import com.google.gson.*;
import io.undertow.server.*;
import io.undertow.util.*;

import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class UtilJson {

  private UtilJson() {}

  private static final Gson gson = new Gson();

  private static final int MAX_BODY_SIZE = 1_048_576;

  public static JsonObject corpo(HttpServerExchange x) throws Exception {

    byte[] bytes = x.getInputStream().readNBytes(
            MAX_BODY_SIZE + 1
    );

    if (bytes.length > MAX_BODY_SIZE) {
      throw new ErroApi(
              413,
              "Corpo da requisição muito grande."
      );
    }

    String raw = new String(
            bytes,
            StandardCharsets.UTF_8
    );

    try {
      JsonElement element = JsonParser.parseString(raw);

      if (!element.isJsonObject()) {

        throw new ErroApi(400, "O corpo deve ser um objeto JSON.");
      }

      return element.getAsJsonObject();

    } catch (ErroApi e) {
      throw e;

    } catch (Exception e) {

      throw new ErroApi(400, "Corpo JSON inválido.");
    }
  }

  public static String parametroConsulta(String q, String key) {

    if (q == null) {
      return "";
    }

    for (String p : q.split("&")) {

      String[] a = p.split("=", 2);

      if (URLDecoder.decode(
              a[0],
              StandardCharsets.UTF_8
      ).equals(key)) {

        return a.length > 1
                ? URLDecoder.decode(
                a[1],
                StandardCharsets.UTF_8
        )
                : "";
      }
    }

    return "";
  }

  public static String s(JsonObject o, String k) {

    return o.has(k) && !o.get(k).isJsonNull()
            ? o.get(k).getAsString()
            : "";
  }

  public static double n(
          JsonObject o,
          String k,
          double d
  ) {

    try {

      return o.has(k) && !o.get(k).isJsonNull()
              ? o.get(k).getAsDouble()
              : d;

    } catch (Exception e) {
      return d;
    }
  }

  public static Double numeroOpcional(JsonObject o, String k) {

    if (!o.has(k) ||
            o.get(k).isJsonNull() ||
            s(o, k).isBlank()) {

      return null;
    }

    return n(o, k, Double.NaN);
  }

  public static JsonObject erro(String m) {

    JsonObject o = new JsonObject();
    o.addProperty("error", m);

    return o;
  }

  public static void enviar(
          HttpServerExchange x,
          int status,
          JsonObject o
  ) {

    if (x.isResponseStarted()) {
      return;
    }

    x.setStatusCode(status);

    x.getResponseHeaders().put(
            Headers.CONTENT_TYPE,
            "application/json; charset=utf-8"
    );

    x.getResponseSender().send(gson.toJson(o));

    x.endExchange();
  }
}
