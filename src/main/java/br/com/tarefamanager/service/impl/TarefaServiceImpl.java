package br.com.tarefamanager.service.impl;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.service.TarefaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TarefaServiceImpl implements TarefaService {

    private final TarefaRepository tarefaRepository;
    private final SubtarefaRepository subtarefaRepository;

    public TarefaServiceImpl(TarefaRepository tarefaRepository, SubtarefaRepository subtarefaRepository) {
        this.tarefaRepository = tarefaRepository;
        this.subtarefaRepository = subtarefaRepository;
    }

    @Override
    @Transactional
    public Tarefa criarTarefa(Tarefa tarefa) {
        tarefa.setStatus(StatusTarefa.PENDENTE);
        return tarefaRepository.save(tarefa);
    }

    @Override
    public List<Tarefa> listarPorStatus(StatusTarefa status) {
        if (status == null) {
            return tarefaRepository.findAll(); // retorna todas as tarefas
        }
        return tarefaRepository.findByStatus(status);
    }


    @Override
    @Transactional
    public Tarefa atualizarStatus(String tarefaId, StatusTarefa novoStatus) {
        Tarefa tarefa = tarefaRepository.findById(tarefaId)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));

        if (novoStatus == StatusTarefa.CONCLUIDA) {
            // Verifica se todas as subtarefas estão concluídas
            boolean possuiPendentes = subtarefaRepository.existsByTarefaIdAndStatusNot(tarefaId, StatusTarefa.CONCLUIDA);
            if (possuiPendentes) {
                throw new RuntimeException("Não é possível concluir a tarefa com subtarefas pendentes");
            }
            tarefa.setDataConclusao(LocalDateTime.now());
        } else {
            tarefa.setDataConclusao(null);
        }

        tarefa.setStatus(novoStatus);
        return tarefaRepository.save(tarefa);
    }
}
