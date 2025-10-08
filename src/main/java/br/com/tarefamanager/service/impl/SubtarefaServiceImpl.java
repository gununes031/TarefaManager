package br.com.tarefamanager.service.impl;

import br.com.tarefamanager.exception.SubtarefaNotFoundException;
import br.com.tarefamanager.exception.TarefaNotFoundException;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.service.SubtarefaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SubtarefaServiceImpl implements SubtarefaService {

    private final SubtarefaRepository subtarefaRepository;
    private final TarefaRepository tarefaRepository;

    public SubtarefaServiceImpl(SubtarefaRepository subtarefaRepository, TarefaRepository tarefaRepository) {
        this.subtarefaRepository = subtarefaRepository;
        this.tarefaRepository = tarefaRepository;
    }

    @Override
    @Transactional
    public Subtarefa criarSubtarefa(String tarefaId, Subtarefa subtarefa) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new TarefaNotFoundException("Tarefa não encontrada"));
        subtarefa.setTarefa(tarefa);
        subtarefa.setStatus(StatusTarefa.PENDENTE);
        return subtarefaRepository.save(subtarefa);
    }

    @Override
    public List<Subtarefa> listarSubtarefas(String tarefaId) {
        return subtarefaRepository.findByTarefaId(tarefaId);
    }

    @Override
    @Transactional
    public Subtarefa atualizarStatus(String subtarefaId, StatusTarefa novoStatus) {
        Subtarefa subtarefa = subtarefaRepository.findById(subtarefaId)
                .orElseThrow(() -> new SubtarefaNotFoundException("Subtarefa não encontrada"));

        if (novoStatus != StatusTarefa.CONCLUIDA) {
            subtarefa.setDataConclusao(LocalDateTime.now());
        } else {
            subtarefa.setDataConclusao(null);
        }

        subtarefa.setStatus(novoStatus);
        return subtarefaRepository.save(subtarefa);
    }

    @Override
    public Page<Subtarefa> listarSubtarefas(String tarefaId, StatusTarefa status, Pageable pageable) {
        if (tarefaId != null && status != null) {
            return subtarefaRepository.findByTarefaIdAndStatus(tarefaId, status, pageable);
        } else if (tarefaId != null) {
            return subtarefaRepository.findByTarefaId(tarefaId, pageable);
        } else if (status != null) {
            return subtarefaRepository.findByStatus(status, pageable);
        } else {
            return subtarefaRepository.findAll(pageable);
        }
    }
}
