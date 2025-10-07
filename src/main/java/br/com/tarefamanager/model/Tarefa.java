package br.com.tarefamanager.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Tarefa {

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
    private Usuario usuario;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Subtarefa> subtarefas;
}
