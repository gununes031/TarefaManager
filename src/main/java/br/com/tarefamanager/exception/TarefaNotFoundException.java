package br.com.tarefamanager.exception;

public class TarefaNotFoundException extends RuntimeException {
    public TarefaNotFoundException(String message) {
        super(message);
    }
}