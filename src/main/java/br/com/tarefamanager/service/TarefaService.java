package br.com.tarefamanager.service;

import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.StatusTarefa;

import java.util.List;

public interface TarefaService {
    Tarefa criarTarefa(Tarefa tarefa);
    List<Tarefa> listarPorStatus(StatusTarefa status);
    Tarefa atualizarStatus(String tarefaId, StatusTarefa novoStatus);
}
