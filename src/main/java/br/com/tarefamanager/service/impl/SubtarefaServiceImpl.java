package br.com.tarefamanager.service.impl;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.service.SubtarefaService;
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
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
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
                .orElseThrow(() -> new RuntimeException("Subtarefa não encontrada"));

        if (novoStatus == StatusTarefa.CONCLUIDA) {
            subtarefa.setDataConclusao(LocalDateTime.now());
        } else {
            subtarefa.setDataConclusao(null);
        }

        subtarefa.setStatus(novoStatus);
        return subtarefaRepository.save(subtarefa);
    }
}
