package br.com.quemresolve.intermediario;

import io.undertow.*;
import io.undertow.server.handlers.PathHandler;

import br.com.quemresolve.intermediario.configuracao.ConfiguracaoCors;
import br.com.quemresolve.intermediario.configuracao.ConfiguracaoServidor;
import br.com.quemresolve.intermediario.banco.ClienteDados;
import br.com.quemresolve.intermediario.rotas.RotasApi;
import br.com.quemresolve.intermediario.websocket.SocketTempoReal;

public class Main {
  public static void main(String[] args) {
    ConfiguracaoServidor config=new ConfiguracaoServidor();
    String dbHost=config.dbHost;

    int dbPort=config.dbPort;
    int port=config.port;

    String host=config.host;
    String origins=config.origins;
    boolean cookieSeguro=config.cookieSeguro;

    ClienteDados dados=new ClienteDados(dbHost,dbPort);

    RotasApi api=new RotasApi(dados,new ConfiguracaoCors(origins),cookieSeguro);

    PathHandler paths=io.undertow.Handlers.path()
      .addExactPath("/api/ao-vivo",io.undertow.Handlers.websocket(new SocketTempoReal(dados)))
      .addPrefixPath("/api",api);

    Undertow server=Undertow.builder().addHttpListener(port,host).setHandler(paths).build();

    server.start();
    System.out.println("Backend intermediário HTTP/WebSocket em "+host+":"+port);
  }
}
