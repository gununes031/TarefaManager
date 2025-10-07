package br.com.tarefamanager.service;

import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.StatusTarefa;

import java.util.List;

public interface SubtarefaService {
    Subtarefa criarSubtarefa(String tarefaId, Subtarefa subtarefa);
    List<Subtarefa> listarSubtarefas(String tarefaId);
    Subtarefa atualizarStatus(String subtarefaId, StatusTarefa novoStatus);
}