package br.com.tarefamanager.integration;

import br.com.tarefamanager.model.Usuario;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        usuarioRepository.deleteAll();
    }

    @Test
    void deveCriarUsuarioComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@email.com");

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.nome").value("Gustavo"))
                .andExpect(jsonPath("$.email").value("gustavo@email.com"));
    }

    @Test
    void deveFalharAoCriarUsuarioComEmailJaCadastrado() throws Exception {
        Usuario existente = new Usuario();
        existente.setNome("Outro");
        existente.setEmail("existente@email.com");
        usuarioRepository.save(existente);

        Usuario novo = new Usuario();
        novo.setNome("Gustavo");
        novo.setEmail("existente@email.com");

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(novo)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Já existe um usuário cadastrado com o email enviado")));
    }

    @Test
    void deveFalharAoCriarUsuarioComCamposInvalidos() throws Exception {
        Usuario usuario = new Usuario(); // nome e email em branco

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Erro de validação")));
    }

    @Test
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@email.com");
        Usuario salvo = usuarioRepository.save(usuario);

        mockMvc.perform(get("/usuarios/{id}", salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.nome").value("Gustavo"))
                .andExpect(jsonPath("$.email").value("gustavo@email.com"));
    }

    @Test
    void deveFalharAoBuscarUsuarioInexistente() throws Exception {
        mockMvc.perform(get("/usuarios/{id}", "id-inexistente"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Usuário não encontrado")));
    }
}
