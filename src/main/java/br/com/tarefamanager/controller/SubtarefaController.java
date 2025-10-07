package br.com.tarefamanager.controller;

import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.service.SubtarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
public class SubtarefaController {

    private final SubtarefaService subtarefaService;

    public SubtarefaController(SubtarefaService subtarefaService) {
        this.subtarefaService = subtarefaService;
    }

    @PostMapping("/tarefas/{tarefaId}/subtarefas")
    public ResponseEntity<Subtarefa> criar(@PathVariable String tarefaId,
                                           @Valid @RequestBody Subtarefa subtarefa) {
        Subtarefa criada = subtarefaService.criarSubtarefa(tarefaId, subtarefa);
        return new ResponseEntity<>(criada, HttpStatus.CREATED);
    }

    @GetMapping("/tarefas/{tarefaId}/subtarefas")
    public ResponseEntity<List<Subtarefa>> listar(@PathVariable String tarefaId) {
        List<Subtarefa> subtarefas = subtarefaService.listarSubtarefas(tarefaId);
        return ResponseEntity.ok(subtarefas);
    }

    @PatchMapping("/subtarefas/{id}/status")
    public ResponseEntity<Subtarefa> atualizarStatus(@PathVariable String id,
                                                     @RequestParam StatusTarefa status) {
        Subtarefa atualizada = subtarefaService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizada);
    }
}
