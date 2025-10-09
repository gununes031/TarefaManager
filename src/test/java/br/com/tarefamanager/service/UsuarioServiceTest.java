package br.com.tarefamanager.service;

import br.com.tarefamanager.exception.EmailJaCadastradoException;
import br.com.tarefamanager.exception.UsuarioNotFoundException;
import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.repository.UsuarioRepository;
import br.com.tarefamanager.service.impl.UsuarioServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Validator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("✅ Deve criar usuário com sucesso")
    void deveCriarUsuarioComSucesso() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@example.com");

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // When
        Usuario resultado = usuarioService.criarUsuario(usuario);

        // Then
        assertNotNull(resultado);
        assertEquals("Gustavo", resultado.getNome());
        assertEquals("gustavo@example.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    @DisplayName("🚫 Deve lançar exceção ao tentar criar usuário com e-mail duplicado")
    void deveLancarExcecaoAoCriarUsuarioComEmailDuplicado() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@example.com");

        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        // When / Then
        assertThrows(EmailJaCadastradoException.class, () -> usuarioService.criarUsuario(usuario));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("✅ Deve buscar usuário existente com sucesso")
    void deveBuscarUsuarioExistente() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setId("123");
        usuario.setNome("Gustavo");

        when(usuarioRepository.findById("123")).thenReturn(Optional.of(usuario));

        // When
        Usuario resultado = usuarioService.buscarPorId("123");

        // Then
        assertNotNull(resultado);
        assertEquals("Gustavo", resultado.getNome());
        verify(usuarioRepository, times(1)).findById("123");
    }

    @Test
    @DisplayName("🚫 Deve lançar exceção ao buscar usuário inexistente")
    void deveLancarExcecaoAoBuscarUsuarioInexistente() {
        // Given
        String idInvalido = "999";
        when(usuarioRepository.findById(idInvalido)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(UsuarioNotFoundException.class, () -> usuarioService.buscarPorId(idInvalido));
        verify(usuarioRepository, times(1)).findById(idInvalido);
    }

    @Test
    @DisplayName("🚫 Deve falhar ao criar usuário com nome em branco")
    void deveFalharAoCriarUsuarioComNomeEmBranco() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("");
        usuario.setEmail("gustavo@example.com");

        // When
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

        // Then
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("nome")));
    }

    @Test
    @DisplayName("🚫 Deve falhar ao criar usuário com email em branco")
    void deveFalharAoCriarUsuarioComEmailEmBranco() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("");

        // When
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

        // Then
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    @DisplayName("🚫 Deve falhar ao criar usuário com email inválido")
    void deveFalharAoCriarUsuarioComEmailInvalido() {
        // Given
        Usuario usuario = new Usuario();
        usuario.setNome("Gustavo");
        usuario.setEmail("email-invalido");

        // When
        Set<ConstraintViolation<Usuario>> violacoes = validator.validate(usuario);

        // Then
        assertFalse(violacoes.isEmpty());
        assertTrue(violacoes.stream().anyMatch(v -> v.getMessage().contains("deve ser um endereço de e-mail bem formado")));
    }
}
