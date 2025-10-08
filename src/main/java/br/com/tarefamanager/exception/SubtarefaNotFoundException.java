package br.com.tarefamanager.exception;

public class SubtarefaNotFoundException extends RuntimeException {
    public SubtarefaNotFoundException(String message) {
        super(message);
    }
}