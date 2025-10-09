package br.com.tarefamanager.service;

import br.com.tarefamanager.exception.BusinessException;
import br.com.tarefamanager.exception.RelatedEntityNotFoundException;
import br.com.tarefamanager.exception.TarefaNotFoundException;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.model.Usuario;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.repository.UsuarioRepository;
import br.com.tarefamanager.service.impl.TarefaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TarefaServiceTest {

    @Mock
    private TarefaRepository tarefaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @InjectMocks
    private TarefaServiceImpl tarefaService;

    private Usuario usuario;
    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("1");
        usuario.setNome("Gustavo");
        usuario.setEmail("gustavo@email.com");

        tarefa = new Tarefa();
        tarefa.setId("1");
        tarefa.setTitulo("Estudar Spring");
        tarefa.setDescricao("Ler documentação");
        tarefa.setStatus(StatusTarefa.PENDENTE);
        tarefa.setUsuario(usuario);
    }

    @Test
    void deveCriarTarefaComSucesso() {
        // Given
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        // When
        Tarefa resultado = tarefaService.criarTarefa(tarefa);

        // Then
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTitulo()).isEqualTo("Estudar Spring");
        verify(tarefaRepository, times(1)).save(any(Tarefa.class));
    }

    @Test
    void deveLancarExcecaoAoCriarTarefaComUsuarioInvalido() {
        // Given
        tarefa.setUsuario(new Usuario());
        tarefa.getUsuario().setId("999");
        when(usuarioRepository.findById("999")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> tarefaService.criarTarefa(tarefa))
                .isInstanceOf(RelatedEntityNotFoundException.class);
    }

    @Test
    void deveAtualizarStatusParaConcluida() {
        // Given
        tarefa.setStatus(StatusTarefa.EM_ANDAMENTO);
        when(tarefaRepository.findById("1")).thenReturn(Optional.of(tarefa));
        when(tarefaRepository.save(any(Tarefa.class))).thenReturn(tarefa);

        // When
        Tarefa resultado = tarefaService.atualizarStatus("1", StatusTarefa.CONCLUIDA);

        // Then
        assertThat(resultado.getStatus()).isEqualTo(StatusTarefa.CONCLUIDA);
        assertThat(resultado.getDataConclusao()).isNotNull();
    }

    @Test
    void deveLancarExcecaoAoAtualizarTarefaInvalida() {
        // Given
        when(tarefaRepository.findById("999")).thenReturn(Optional.empty());

        // When / Then
        assertThatThrownBy(() -> tarefaService.atualizarStatus("999", StatusTarefa.CONCLUIDA))
                .isInstanceOf(TarefaNotFoundException.class);
    }

    @Test
    void deveLancarExcecaoAoConcluirTarefaComSubtarefasPendentes() {
        // Given
        Subtarefa sub1 = new Subtarefa();
        sub1.setId("s1");
        sub1.setTitulo("Sub 1");
        sub1.setStatus(StatusTarefa.PENDENTE);
        sub1.setTarefa(tarefa);

        tarefa.setSubtarefas(List.of(sub1));
        when(tarefaRepository.findById("1")).thenReturn(Optional.of(tarefa));
        when(subtarefaRepository.existsByTarefaIdAndStatusNot(tarefa.getId(), StatusTarefa.CONCLUIDA))
                .thenReturn(true);

        // When / Then
        assertThatThrownBy(() -> tarefaService.atualizarStatus("1", StatusTarefa.CONCLUIDA))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    void deveFiltrarTarefasPorStatusEUsuario() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Tarefa> page = new PageImpl<>(List.of(tarefa));

        when(tarefaRepository.findByUsuarioIdAndStatus("1", StatusTarefa.PENDENTE, pageable))
                .thenReturn(page);

        // When
        Page<Tarefa> resultado = tarefaService.listarTarefas("1", StatusTarefa.PENDENTE, pageable);

        // Then
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getTitulo()).isEqualTo("Estudar Spring");
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistemTarefas() {
        // Given
        Pageable pageable = PageRequest.of(1, 5);
        when(tarefaRepository.findByUsuarioIdAndStatus("1", StatusTarefa.CONCLUIDA, pageable))
                .thenReturn(Page.empty());

        // When
        Page<Tarefa> resultado = tarefaService.listarTarefas("1", StatusTarefa.CONCLUIDA, pageable);

        // Then
        assertThat(resultado.getContent()).isEmpty();
    }
}
