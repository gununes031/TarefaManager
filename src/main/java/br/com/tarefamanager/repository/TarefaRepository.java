package br.com.tarefamanager.repository;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TarefaRepository extends JpaRepository<Tarefa, String> {
    List<Tarefa> findByStatus(StatusTarefa status);

    Page<Tarefa> findByUsuarioId(String usuarioId, Pageable pageable);

    Page<Tarefa> findByStatus(StatusTarefa status, Pageable pageable);

    Page<Tarefa> findByUsuarioIdAndStatus(String usuarioId, StatusTarefa status, Pageable pageable);
}