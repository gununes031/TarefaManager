package br.com.tarefamanager.integration;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TarefaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private SubtarefaRepository subtarefaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Usuario usuario;

    @BeforeEach
    void setup() {
        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@email.com");
        usuario = usuarioRepository.save(usuario);
    }

    @Test
    void deveCriarTarefaComSucesso() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa 1");
        tarefa.setDescricao("Descrição da tarefa");
        tarefa.setUsuario(usuario);
        tarefa.setStatus(StatusTarefa.PENDENTE);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.titulo").value("Tarefa 1"))
                .andExpect(jsonPath("$.usuario.id").value(usuario.getId()));
    }

    @Test
    void deveFalharAtualizarStatusComSubtarefasPendentes() throws Exception {
        // cria a tarefa
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa com subtarefa pendente");
        tarefa.setDescricao("Descrição");
        tarefa.setUsuario(usuario);
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa = tarefaRepository.save(tarefa);

        // cria uma subtarefa pendente associada
        br.com.tarefamanager.model.Subtarefa subtarefa = new br.com.tarefamanager.model.Subtarefa();
        subtarefa.setTitulo("Subtarefa pendente");
        subtarefa.setDescricao("Descrição da subtarefa");
        subtarefa.setStatus(StatusTarefa.PENDENTE);
        subtarefa.setTarefa(tarefa);
        // supondo que você tenha SubtarefaRepository injetado:
        subtarefaRepository.save(subtarefa);

        mockMvc.perform(patch("/tarefas/{id}/status", tarefa.getId())
                        .param("status", "CONCLUIDA"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Não é possível concluir a tarefa com subtarefas pendentes")));
    }


    @Test
    void deveFalharCriarTarefaComUsuarioInvalido() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa invalida");
        tarefa.setDescricao("Descrição");
        Usuario fake = new Usuario();
        fake.setId("id-invalido");
        tarefa.setUsuario(fake);

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tarefa)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Usuário não encontrado")));
    }

    @Test
    void deveAtualizarStatusParaConcluida() throws Exception {
        Tarefa tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa a concluir");
        tarefa.setDescricao("Descrição");
        tarefa.setUsuario(usuario);
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa = tarefaRepository.save(tarefa);

        mockMvc.perform(patch("/tarefas/{id}/status", tarefa.getId())
                        .param("status", "CONCLUIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void deveFalharAtualizarStatusComTarefaInexistente() throws Exception {
        mockMvc.perform(patch("/tarefas/{id}/status", "id-inexistente")
                        .param("status", "CONCLUIDA"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Tarefa não encontrada")));
    }

    @Test
    void deveListarTarefasComPaginacaoEFiltro() throws Exception {
        // cria duas tarefas
        Tarefa t1 = new Tarefa();
        t1.setTitulo("T1");
        t1.setDescricao("Desc1");
        t1.setUsuario(usuario);
        t1.setStatus(StatusTarefa.PENDENTE);

        Tarefa t2 = new Tarefa();
        t2.setTitulo("T2");
        t2.setDescricao("Desc2");
        t2.setUsuario(usuario);
        t2.setStatus(StatusTarefa.CONCLUIDA);

        tarefaRepository.saveAll(java.util.List.of(t1, t2));

        mockMvc.perform(get("/tarefas")
                        .param("usuarioId", usuario.getId())
                        .param("status", "PENDENTE")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].titulo").value("T1"));
    }
}
