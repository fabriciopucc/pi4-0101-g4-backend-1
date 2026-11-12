package br.com.quemresolve.intermediario.configuracao;

public class ConfiguracaoServidor {
  public final String dbHost=env("DB_SERVER_HOST","127.0.0.1");
  public final int dbPort=Integer.parseInt(env("DB_SERVER_PORT","8081"));
  public final int port=Integer.parseInt(env("HTTP_PORT","8080"));
  public final String host=env("HTTP_HOST","0.0.0.0");
  public final String origins=env("ALLOWED_ORIGINS","http://localhost:5173,http://127.0.0.1:5173,http://localhost:3000,http://127.0.0.1:3000");
  public final boolean cookieSeguro=Boolean.parseBoolean(env("SECURE_COOKIE","false"));

  static String env(String k,String d){String v=System.getenv(k);return v==null||v.isBlank()?d:v;}
}
