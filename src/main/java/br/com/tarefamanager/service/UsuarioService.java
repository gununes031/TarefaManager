package br.com.tarefamanager.service;

import br.com.tarefamanager.model.Usuario;

public interface UsuarioService {
    Usuario criarUsuario(Usuario usuario);
    Usuario buscarPorId(String id);
}