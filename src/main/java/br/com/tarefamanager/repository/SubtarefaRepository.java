package br.com.tarefamanager.repository;

import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.StatusTarefa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtarefaRepository extends JpaRepository<Subtarefa, String> {
    List<Subtarefa> findByTarefaId(String tarefaId);
    boolean existsByTarefaIdAndStatusNot(String tarefaId, StatusTarefa status);
}