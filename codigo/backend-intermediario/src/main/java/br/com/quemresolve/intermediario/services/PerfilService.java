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

public class PerfilService {

  private final ClienteDados dados;
  private final SessaoService sessoes;

  private static final Set<String> CATS = Set.of(
          "Eletricista",
          "Encanador",
          "Limpeza",
          "Pintor",
          "Jardinagem",
          "Montador",
          "Ar-condicionado",
          "Reformas"
  );

  private static final Set<String> UNIDADES = Set.of(
          "por serviço",
          "por hora",
          "por diária",
          "a combinar"
  );

  public PerfilService(ClienteDados dados, SessaoService sessoes) {
    this.dados = dados;
    this.sessoes = sessoes;
  }

  public JsonObject salvarPerfil(HttpServerExchange x) throws Exception {

    JsonObject u = sessoes.autenticar(x);

    if (!s(u, "role").equals("prestador")) {

      throw new ErroApi(403, "Apenas prestadores podem editar perfis profissionais.");
    }

    JsonObject b = corpo(x);

    String categoria = s(b, "categoria").trim();
    String titulo = s(b, "titulo").trim();
    String biografia = s(b, "biografia").trim();
    String cidade = s(b, "cidade").trim();
    String regiao = s(b, "regiao").trim();
    String unidade = s(b, "unidade");
    String celular = s(b, "celular").replaceAll("\\D", "");

    double preco = n(b, "preco", Double.NaN);
    double raio = n(b, "raio", Double.NaN);

    Double latitude = numeroOpcional(b, "latitude");
    Double longitude = numeroOpcional(b, "longitude");

    boolean categoriass = !categoria.isBlank() &&
            Arrays.stream(categoria.split(",", -1))
                    .map(String::trim)
                    .allMatch(CATS::contains);

    if (!categoriass){
      throw new ErroApi(400, "Escolha ao menos 1 categoria.");
    }

    if(titulo.isBlank() || titulo.length() > 100){
      throw new ErroApi(400, "O titulo não pode ser nulo nem passar de 100 caracteres!.");
    }

    if(biografia.isBlank() ||  biografia.length() > 2000){
      throw new ErroApi(400, "A biografia não pode ser nulo nem passar de 2000 caracteres!.");
    }

    if(cidade.isBlank() || cidade.length() > 100){
      throw new ErroApi(400, "A cidade não pode ser nula nem passar de 100 caracteres!.");
    }

    if(regiao.isBlank() || regiao.length() > 300){
      throw new ErroApi(400, "A região não pode ser nula nem passar de 300 caracteres!.");
    }

    if(!Double.isFinite(preco) || preco < 0 ||preco > 1_000_000){
      throw new ErroApi(400, "O preço não pode ser nulo e deve entrar entre 1 e 1.000.000!.");
    }
    if(raio < 1 || raio > 500){
      throw new ErroApi(400, "O raio não pode ser nulo e deve entrar entre 1 e 500!.");
    }

    if(!celular.matches("^\\d{10,11}$")) {
      throw new ErroApi(400, "Digite um número válido.");
    }

    if(!UNIDADES.contains(unidade.toLowerCase().trim())){
      throw new ErroApi(400, "Digite uma unidade válida! (por serviço, por hora, por diária, a combinar)");
    }

    if((latitude != null && (!Double.isFinite(latitude) || Math.abs(latitude) > 90)) ||
       (longitude != null && (!Double.isFinite(longitude) || Math.abs(longitude) > 180)) ||
       ((latitude == null) != (longitude == null))) {

      throw new ErroApi(400, "Latitude ou longitude inválidos.");
    }

    JsonObject objeto = ClienteDados.objeto(
 "usuario_Id", s(u, "id"),
        "categoria", categoria,
        "titulo", titulo,
        "biografia", biografia,
        "cidade", cidade,
        "regiao", regiao,
        "unidade", unidade,
        "celular", celular,
        "raio", Double.toString(raio),
        "preco", Double.toString(preco),
        "atualizado", Long.toString(System.currentTimeMillis())
    );

    if (latitude == null) {
      objeto.add("latitude", JsonNull.INSTANCE);
    } else {
      objeto.addProperty("latiude", latitude);
    }

    if (longitude == null) {
      objeto.add("lng", JsonNull.INSTANCE);
    } else {
      objeto.addProperty("lng", longitude);
    }

    dados.chamar("UPSERT_PROFILE", objeto);

    JsonObject resposta = new JsonObject();
    resposta.addProperty("ok", true);

    return resposta;
  }
}
