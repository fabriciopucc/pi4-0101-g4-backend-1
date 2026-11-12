package br.com.quemresolve.intermediario.banco;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class ClienteDados {
  private final String host; private final int port; private final Gson gson=new Gson();
  public ClienteDados(String h,int p){host=h;port=p;}
  public JsonObject chamar(String op,JsonObject data){
    JsonObject requisicao=new JsonObject();requisicao.addProperty("op",op);requisicao.add("data",data);
    try(Socket s=new Socket()){
      s.connect(new InetSocketAddress(host,port),3000);s.setSoTimeout(7000);
      try(PrintWriter out=new PrintWriter(new OutputStreamWriter(s.getOutputStream(),StandardCharsets.UTF_8),true);
          BufferedReader in=new BufferedReader(new InputStreamReader(s.getInputStream(),StandardCharsets.UTF_8))){
        out.println(gson.toJson(requisicao));String linha=in.readLine();if(linha==null)throw new IOException("Resposta vazia");
        JsonObject resultado=JsonParser.parseString(linha).getAsJsonObject();
        if(resultado.has("error"))throw new IllegalStateException(resultado.get("error").getAsString());
        return resultado;
      }
    }catch(Exception e){throw new IllegalStateException("Falha ao chamar backend de dados: "+e.getMessage(),e);}
  }
  public static JsonObject objeto(String... pares){JsonObject o=new JsonObject();for(int i=0;i<pares.length;i+=2)o.addProperty(pares[i],pares[i+1]);return o;}
  public static boolean encontrado(JsonObject o){return o.has("found")&&o.get("found").getAsBoolean();}
}
