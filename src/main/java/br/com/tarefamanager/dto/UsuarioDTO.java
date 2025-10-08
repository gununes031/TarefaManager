package br.com.tarefamanager.dto;

import br.com.tarefamanager.model.Usuario;

public record UsuarioDTO(
        String id,
        String nome,
        String email
) {
    public static UsuarioDTO from(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getEmail());
    }
}

