package br.com.tarefamanager.controller;

import br.com.tarefamanager.dto.SubtarefaDTO;
import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.service.SubtarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Operation(
            summary = "Cria uma subtarefa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subtarefa criada com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Subtarefa invalida")
            }
    )
    @PostMapping("/tarefas/{tarefaId}/subtarefas")
    public ResponseEntity<Subtarefa> criar(@PathVariable String tarefaId,
                                           @Valid @RequestBody Subtarefa subtarefa) {
        Subtarefa criada = subtarefaService.criarSubtarefa(tarefaId, subtarefa);
        return new ResponseEntity<>(criada, HttpStatus.CREATED);
    }

//    @Operation(
//            summary = "Consulta subtarefas",
//            responses = {
//                    @ApiResponse(responseCode = "200", description = "Subtarefas encontradas"),
//                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
//            }
//    )
//    @GetMapping("/tarefas/{tarefaId}/subtarefas")
//    public ResponseEntity<List<SubtarefaDTO>> listar(@PathVariable String tarefaId) {
//        List<Subtarefa> subtarefas = subtarefaService.listarSubtarefas(tarefaId);
//        List<SubtarefaDTO> dtos = subtarefas.stream()
//                .map(SubtarefaDTO::from)
//                .toList();
//        return ResponseEntity.ok(dtos);
//    }

    @Operation(
            summary = "Atualiza o status de uma tarefa",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
                    @ApiResponse(responseCode = "404", description = "Subtarefa não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Atualização invalida")
            }
    )
    @PatchMapping("/subtarefas/{id}/status")
    public ResponseEntity<Subtarefa> atualizarStatus(@PathVariable String id,
                                                     @RequestParam StatusTarefa status) {
        Subtarefa atualizada = subtarefaService.atualizarStatus(id, status);
        return ResponseEntity.ok(atualizada);
    }

    @Operation(
            summary = "Consulta subtarefas",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subtarefas encontradas"),
                    @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
            }
    )
    @GetMapping("/tarefas/{tarefaId}/subtarefas")
    public ResponseEntity<Page<SubtarefaDTO>> listar(
            @PathVariable String tarefaId,
            @RequestParam(required = false) StatusTarefa status,
            @PageableDefault(size = 10) Pageable pageable) {

        Page<Subtarefa> page = subtarefaService.listarSubtarefas(tarefaId, status, pageable);
        Page<SubtarefaDTO> pageDTO = page.map(SubtarefaDTO::fromEntity);

        return ResponseEntity.ok(pageDTO);
    }
}
