package br.com.tarefamanager.service.impl;

import br.com.tarefamanager.exception.BusinessException;
import br.com.tarefamanager.exception.RelatedEntityNotFoundException;
import br.com.tarefamanager.exception.TarefaNotFoundException;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.repository.UsuarioRepository;
import br.com.tarefamanager.service.TarefaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final SubtarefaRepository subtarefaRepository;
    private final UsuarioRepository usuarioRepository;


    public TarefaServiceImpl(TarefaRepository tarefaRepository, SubtarefaRepository subtarefaRepository, UsuarioRepository usuarioRepository) {
        this.tarefaRepository = tarefaRepository;
        this.subtarefaRepository = subtarefaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa) {
        if (tarefa.getUsuario() == null || tarefa.getUsuario().getId() == null) {
            throw new RelatedEntityNotFoundException("Usuário não informado para a tarefa");
        }

        Usuario usuario = usuarioRepository.findById(tarefa.getUsuario().getId())
                .orElseThrow(() -> new RelatedEntityNotFoundException("Usuário não encontrado"));

        tarefa.setUsuario(usuario);

        return tarefaRepository.save(tarefa);
    }


    @Override
    public List<Tarefa> listarPorStatus(StatusTarefa status) {
        if (status == null) {
            return tarefaRepository.findAll();
        }
        return tarefaRepository.findByStatus(status);
    }


    @Override
    @Transactional
    public Tarefa atualizarStatus(String tarefaId, StatusTarefa novoStatus) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new TarefaNotFoundException("Tarefa não encontrada"));

        if (novoStatus == StatusTarefa.CONCLUIDA) {
            boolean possuiPendentes = subtarefaRepository.existsByTarefaIdAndStatusNot(tarefaId, StatusTarefa.CONCLUIDA);
            if (possuiPendentes) {
                throw new BusinessException("Não é possível concluir a tarefa com subtarefas pendentes");
            }
            tarefa.setDataConclusao(LocalDateTime.now());
        } else {
            tarefa.setDataConclusao(null);
        }

        tarefa.setStatus(novoStatus);
        return tarefaRepository.save(tarefa);
    }


    @Override
    public Page<Tarefa> listarTarefas(String usuarioId, StatusTarefa status, Pageable pageable) {
        if (usuarioId != null && status != null) {
            return tarefaRepository.findByUsuarioIdAndStatus(usuarioId, status, pageable);
        } else if (usuarioId != null) {
            return tarefaRepository.findByUsuarioId(usuarioId, pageable);
        } else if (status != null) {
            return tarefaRepository.findByStatus(status, pageable);
        } else {
            return tarefaRepository.findAll(pageable);
        }
    }
}
