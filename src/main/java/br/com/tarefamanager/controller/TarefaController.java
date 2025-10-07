package br.com.tarefamanager.controller;

import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.service.TarefaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody Tarefa tarefa) {
        Tarefa criada = tarefaService.criarTarefa(tarefa);
        return new ResponseEntity<>(criada, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar(@RequestParam(required = false) StatusTarefa status) {
        if (status != null) {
            return ResponseEntity.ok(tarefaService.listarPorStatus(status));
        }
        return ResponseEntity.ok(tarefaService.listarPorStatus(null)); // lista todas
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(@PathVariable String id,
                                                  @RequestParam StatusTarefa status) {
        Tarefa atualizada = tarefaService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizada);
    }
}
