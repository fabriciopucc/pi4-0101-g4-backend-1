package br.com.quemresolve.intermediario.seguranca;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.HexFormat;
import java.util.UUID;

public final class Seguranca {
  private Seguranca(){}
  public static String gerarToken() {
    return UUID.randomUUID().toString()
            + UUID.randomUUID().toString();
  }
  public static String calcularSha(String s){try{return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new RuntimeException(e);}}
  public static String resumirSenha(String senha){
    byte[] salt=new byte[16];new SecureRandom().nextBytes(salt);
    String saltHex=HexFormat.of().formatHex(salt);
    return saltHex+":"+derivarChave(senha,saltHex);
  }
  public static boolean verificarSenha(String senha,String armazenado){
    int i=armazenado==null?-1:armazenado.indexOf(':');if(i<1)return false;
    String actual=derivarChave(senha,armazenado.substring(0,i));
    return MessageDigest.isEqual(actual.getBytes(StandardCharsets.US_ASCII),armazenado.substring(i+1).getBytes(StandardCharsets.US_ASCII));
  }
  private static String derivarChave(String senha,String salt){
    try{
      Mac mac=Mac.getInstance("HmacSHA256");mac.init(new SecretKeySpec(senha.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));
      byte[] s=salt.getBytes(StandardCharsets.UTF_8);byte[] first=ByteBuffer.allocate(s.length+4).put(s).putInt(1).array();
      byte[] u=mac.doFinal(first),t=u.clone();
      for(int n=1;n<100000;n++){u=mac.doFinal(u);for(int j=0;j<t.length;j++)t[j]^=u[j];}
      return HexFormat.of().formatHex(t);
    }catch(Exception e){throw new RuntimeException(e);}
  }
}
