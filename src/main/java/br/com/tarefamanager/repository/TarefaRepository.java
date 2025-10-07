package br.com.tarefamanager.repository;

import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.StatusTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, String> {
    List<Tarefa> findByStatus(StatusTarefa status);
}