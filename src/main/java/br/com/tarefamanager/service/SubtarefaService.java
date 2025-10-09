package br.com.tarefamanager.service;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubtarefaService {
    Subtarefa criarSubtarefa(String tarefaId, Subtarefa subtarefa);
    List<Subtarefa> listarSubtarefas(String tarefaId);
    Subtarefa atualizarStatus(String subtarefaId, StatusTarefa novoStatus);

    Page<Subtarefa> listarSubtarefas(String tarefaId, StatusTarefa status, Pageable pageable);
}