package br.com.tarefamanager.controller;

import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Tag(
        name = "Usuarios",
        description = "Endpoints para criação e consulta de usuarios"
)
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
            summary = "Cria um usuario",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario criado com sucesso"),
                    @ApiResponse(responseCode = "400", description = "Usuario invalido")
            }
    )
    @PostMapping
    public ResponseEntity<Usuario> criar(@Valid @RequestBody Usuario usuario) {
        Usuario criado = usuarioService.criarUsuario(usuario);
        return new ResponseEntity<>(criado, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Busca um usuario por id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
                    @ApiResponse(responseCode = "404", description = "Usuario não encontrado")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscar(@PathVariable String id) {
        Usuario u = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(u);
    }
}