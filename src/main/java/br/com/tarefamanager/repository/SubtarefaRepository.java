package br.com.tarefamanager.repository;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtarefaRepository extends JpaRepository<Subtarefa, String> {
    List<Subtarefa> findByTarefaId(String tarefaId);
    boolean existsByTarefaIdAndStatusNot(String tarefaId, StatusTarefa status);


    Page<Subtarefa> findByTarefaId(String tarefaId, Pageable pageable);
    Page<Subtarefa> findByTarefaIdAndStatus(String tarefaId, StatusTarefa status, Pageable pageable);
    Page<Subtarefa> findByStatus(StatusTarefa status, Pageable pageable);
}