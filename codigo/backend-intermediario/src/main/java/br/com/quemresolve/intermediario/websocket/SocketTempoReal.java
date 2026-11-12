package br.com.quemresolve.intermediario.websocket;

import br.com.quemresolve.intermediario.banco.ClienteDados;

import com.google.gson.*;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.*;
import io.undertow.websockets.spi.WebSocketHttpExchange;

public class SocketTempoReal implements WebSocketConnectionCallback {
  private final ClienteDados dados; private final Gson gson=new Gson();
  public SocketTempoReal(ClienteDados dados){this.dados=dados;}
  public void onConnect(WebSocketHttpExchange exchange,WebSocketChannel channel){
    channel.getReceiveSetter().set(new AbstractReceiveListener(){
      protected void onFullTextMessage(WebSocketChannel ch,BufferedTextMessage msg){
        if(!msg.getData().equals("sincronizar")){JsonObject e=new JsonObject();e.addProperty("error","Mensagem não reconhecida.");WebSockets.sendText(gson.toJson(e),ch,null);return;}
        try{JsonObject r=dados.chamar("PROVIDERS",new JsonObject());JsonObject o=new JsonObject();o.add("providers",r.get("providers"));WebSockets.sendText(gson.toJson(o),ch,null);}
        catch(Exception ex){JsonObject e=new JsonObject();e.addProperty("error","Atualização indisponível");WebSockets.sendText(gson.toJson(e),ch,null);}
      }
    });
    channel.resumeReceives();
  }
}
