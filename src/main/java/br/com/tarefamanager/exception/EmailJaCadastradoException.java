package br.com.tarefamanager.exception;

public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException(String email) {
        super("Já existe um usuário cadastrado com o email enviado");
    }
}
