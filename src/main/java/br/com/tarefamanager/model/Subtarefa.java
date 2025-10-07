package br.com.tarefamanager.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@Entity
public class Subtarefa {

    @Id
    @UuidGenerator
    private String id;

    @NotBlank
    private String titulo;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status = StatusTarefa.PENDENTE;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private LocalDateTime dataConclusao;

    @ManyToOne
    @JsonBackReference
    private Tarefa tarefa;
}