package br.com.tarefamanager.service.impl;

import br.com.tarefamanager.exception.EmailJaCadastradoException;
import br.com.tarefamanager.exception.UsuarioNotFoundException;
import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.repository.UsuarioRepository;
import br.com.tarefamanager.service.UsuarioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Usuario criarUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new EmailJaCadastradoException(usuario.getEmail());
        }

        return usuarioRepository.save(usuario);
    }


    @Override
    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
    }
}
