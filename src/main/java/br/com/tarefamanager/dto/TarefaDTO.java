package br.com.tarefamanager.dto;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record TarefaDTO(
        String id,
        String titulo,
        String descricao,
        StatusTarefa status,
        LocalDateTime dataCriacao,
        LocalDateTime dataConclusao,
        UsuarioDTO usuario,
        List<SubtarefaDTO> subtarefas
) {
    public static TarefaDTO fromEntity(Tarefa tarefa) {
        return new TarefaDTO(
                tarefa.getId(),
                tarefa.getTitulo(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataConclusao(),
                UsuarioDTO.from(tarefa.getUsuario()),
                tarefa.getSubtarefas() != null
                        ? tarefa.getSubtarefas().stream().map(SubtarefaDTO::fromEntity).collect(Collectors.toList())
                        : List.of()
        );
    }


}
