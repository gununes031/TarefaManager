package br.com.tarefamanager.controller;

import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Usuário",
        description = "Endpoints para criação e consulta de usuários"
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Cria um novo usuário",
            responses = {
                    @ApiResponse(responseCode = "200", description = "usuário criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "usuário invalido")
            }
    )
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        Usuario criado = usuarioService.criarUsuario(usuario);
        return new ResponseEntity<>(criado, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca usuário pelo ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "usuário encontrado"),
                    @ApiResponse(responseCode = "404", description = "usuário não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable String id) {
        Usuario u = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(u);
    }
}