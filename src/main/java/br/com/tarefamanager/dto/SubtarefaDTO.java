package br.com.tarefamanager.dto;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;

import java.time.LocalDateTime;
import java.util.Objects;

public record SubtarefaDTO(
        String id,
        String titulo,
        String descricao,
        StatusTarefa status,
        LocalDateTime dataCriacao,
        LocalDateTime dataConclusao,
        TarefaResumoDTO tarefa
) {
    public static SubtarefaDTO fromEntity(Subtarefa s) {
        return new SubtarefaDTO(
                s.getId(),
                s.getTitulo(),
                s.getDescricao(),
                s.getStatus(),
                s.getDataCriacao(),
                s.getDataConclusao(),
                Objects.nonNull(s.getTarefa())
                        ? new TarefaResumoDTO(s.getTarefa().getId(), s.getTarefa().getTitulo())
                        : null
        );
    }
}
