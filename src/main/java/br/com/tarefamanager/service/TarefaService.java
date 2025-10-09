package br.com.tarefamanager.service;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TarefaService {
    Tarefa criarTarefa(Tarefa tarefa);
    List<Tarefa> listarPorStatus(StatusTarefa status);
    Tarefa atualizarStatus(String tarefaId, StatusTarefa novoStatus);

    Page<Tarefa> listarTarefas(String usuarioId, StatusTarefa status, Pageable pageable);
}
