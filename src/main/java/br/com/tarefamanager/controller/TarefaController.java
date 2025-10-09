package br.com.tarefamanager.controller;

import br.com.tarefamanager.dto.TarefaDTO;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Tarefas",
        description = "Endpoints para criação, listagem e atualização de tarefas"
)
@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @Operation(
            summary = "Cria uma nova tarefa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tarefa criada com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Tarefa Invalida")
            }
    )
    @PostMapping
    public ResponseEntity<Tarefa> criar(@Valid @RequestBody Tarefa tarefa) {
        Tarefa criada = tarefaService.criarTarefa(tarefa);
        return new ResponseEntity<>(criada, HttpStatus.CREATED);
    }

//    @Operation(
//            summary = "Consulta Tarefas",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
//            }
//    )
//    @GetMapping
//    public ResponseEntity<List<TarefaDTO>> listar(
//            @RequestParam(required = false) String usuarioId,
//            @RequestParam(required = false) StatusTarefa status,
//            @PageableDefault(size = 10, page = 0) Pageable pageable) {
//        List<Tarefa> tarefas = tarefaService.listarPorStatus(status);
//        List<TarefaDTO> dtos = tarefas.stream()
//                .map(TarefaDTO::from)
//                .toList();
//        return ResponseEntity.ok(dtos);
//    }

    @Operation(
            summary = "Atualiza o status de uma tarefa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Atualização invalida")
            }
    )
    @PatchMapping("/{id}/status")
    public ResponseEntity<Tarefa> atualizarStatus(@PathVariable String id,
                                                  @RequestParam StatusTarefa status) {
        Tarefa atualizada = tarefaService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(
            summary = "Lista tarefas (com filtros opcionais: usuarioId, status)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
            }
    )
    @GetMapping
    public ResponseEntity<Page<TarefaDTO>> listar(
            @RequestParam(required = false) String usuarioId,
            @RequestParam(required = false) StatusTarefa status,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        Page<Tarefa> page = tarefaService.listarTarefas(usuarioId, status, pageable);

        Page<TarefaDTO> pageDTO = page.map(TarefaDTO::fromEntity);

        return ResponseEntity.ok(pageDTO);
    }
}
