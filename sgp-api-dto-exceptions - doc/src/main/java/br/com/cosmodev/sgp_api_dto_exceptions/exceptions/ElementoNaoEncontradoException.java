package br.com.cosmodev.sgp_api_dto_exceptions.exceptions;

public class ElementoNaoEncontradoException extends RuntimeException {
    public ElementoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
