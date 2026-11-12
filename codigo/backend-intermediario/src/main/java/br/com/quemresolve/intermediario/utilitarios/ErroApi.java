package br.com.quemresolve.intermediario.utilitarios;

// EXCEÇÃO PERSONALIZADA
public class ErroApi extends RuntimeException {

  public final int status;

  public ErroApi(int status, String mensagem) {
    super(mensagem);
    this.status = status;
  }
}
