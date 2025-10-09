package br.com.tarefamanager.integration;

import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SubtarefaControllerIntegrationTest {

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
    private Tarefa tarefa;

    @BeforeEach
    void setup() {
        subtarefaRepository.deleteAll();
        tarefaRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setNome("Usuario Teste");
        usuario.setEmail("usuario@teste.com");
        usuario = usuarioRepository.save(usuario);

        tarefa = new Tarefa();
        tarefa.setTitulo("Tarefa Teste");
        tarefa.setDescricao("Descrição da Tarefa");
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setUsuario(usuario);
        tarefa = tarefaRepository.save(tarefa);
    }

    @Test
    void deveCriarSubtarefa() throws Exception {
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setTitulo("Subtarefa 1");
        subtarefa.setDescricao("Descrição Subtarefa");
        subtarefa.setStatus(StatusTarefa.PENDENTE);

        mockMvc.perform(post("/tarefas/{tarefaId}/subtarefas", tarefa.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtarefa)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Subtarefa 1"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void deveAtualizarStatusDaSubtarefa() throws Exception {
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setTitulo("Subtarefa Atualizar");
        subtarefa.setDescricao("Descrição");
        subtarefa.setStatus(StatusTarefa.PENDENTE);
        subtarefa.setTarefa(tarefa);
        subtarefa = subtarefaRepository.save(subtarefa);

        mockMvc.perform(patch("/subtarefas/{id}/status", subtarefa.getId())
                        .param("status", "CONCLUIDA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDA"));
    }

    @Test
    void deveFalharQuandoTarefaNaoExistirAoCriarSubtarefa() throws Exception {
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setTitulo("Subtarefa Inválida");
        subtarefa.setDescricao("Descrição");
        subtarefa.setStatus(StatusTarefa.PENDENTE);

        mockMvc.perform(post("/tarefas/{tarefaId}/subtarefas", "id-inexistente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subtarefa)))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Tarefa não encontrada")));
    }

    @Test
    void deveFalharQuandoSubtarefaNaoExistirAoAtualizarStatus() throws Exception {
        mockMvc.perform(patch("/subtarefas/{id}/status", "id-inexistente")
                        .param("status", "CONCLUIDA"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveFiltrarSubtarefasPorStatus() throws Exception {
        Subtarefa s1 = new Subtarefa();
        s1.setTitulo("Subtarefa 1");
        s1.setDescricao("Desc");
        s1.setStatus(StatusTarefa.PENDENTE);
        s1.setTarefa(tarefa);
        subtarefaRepository.save(s1);

        Subtarefa s2 = new Subtarefa();
        s2.setTitulo("Subtarefa 2");
        s2.setDescricao("Desc");
        s2.setStatus(StatusTarefa.CONCLUIDA);
        s2.setTarefa(tarefa);
        subtarefaRepository.save(s2);

        mockMvc.perform(get("/tarefas/{tarefaId}/subtarefas", tarefa.getId())
                        .param("status", "CONCLUIDA")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].status").value("CONCLUIDA"))
                .andExpect(jsonPath("$.content.length()").value(1));
    }
}
