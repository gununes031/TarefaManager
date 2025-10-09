package br.com.tarefamanager.service;

import br.com.tarefamanager.exception.SubtarefaNotFoundException;
import br.com.tarefamanager.exception.TarefaNotFoundException;
import br.com.tarefamanager.model.StatusTarefa;
import br.com.tarefamanager.model.Subtarefa;
import br.com.tarefamanager.model.Tarefa;
import br.com.tarefamanager.repository.SubtarefaRepository;
import br.com.tarefamanager.repository.TarefaRepository;
import br.com.tarefamanager.service.impl.SubtarefaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class SubtarefaServiceTest {

    @Mock
    private SubtarefaRepository subtarefaRepository;

    @Mock
    private TarefaRepository tarefaRepository;

    @InjectMocks
    private SubtarefaServiceImpl subtarefaService;

    private Tarefa tarefa;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tarefa = new Tarefa();
        tarefa.setId("tarefa-1");
        tarefa.setTitulo("Tarefa teste");
    }

    // ✅ Criar subtarefa
    @Test
    void deveCriarSubtarefaComSucesso() {
        // Given
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setTitulo("Subtarefa teste");
        subtarefa.setTarefa(tarefa);
        when(tarefaRepository.findById("tarefa-1")).thenReturn(Optional.of(tarefa));
        when(subtarefaRepository.save(any(Subtarefa.class))).thenReturn(subtarefa);

        // When
        Subtarefa result = subtarefaService.criarSubtarefa("tarefa-1", subtarefa);
        assertThat(result.getTitulo()).isEqualTo("Subtarefa teste");



        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTitulo()).isEqualTo("Subtarefa teste");
    }

    // ✅ Atualizar status
    @Test
    void deveAtualizarStatusDaSubtarefa() {
        // Given
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setId("sub-1");
        subtarefa.setStatus(StatusTarefa.PENDENTE);

        when(subtarefaRepository.findById("sub-1")).thenReturn(Optional.of(subtarefa));
        when(subtarefaRepository.save(any(Subtarefa.class))).thenReturn(subtarefa);

        // When
        Subtarefa result = subtarefaService.atualizarStatus("sub-1", StatusTarefa.CONCLUIDA);

        // Then
        assertThat(result.getStatus()).isEqualTo(StatusTarefa.CONCLUIDA);
    }

    // 🚫 Subtarefa não encontrada
    @Test
    void deveLancarExcecaoQuandoSubtarefaNaoExistir() {
        // Given
        when(subtarefaRepository.findById("sub-999")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(SubtarefaNotFoundException.class, () ->
                subtarefaService.atualizarStatus("sub-999", StatusTarefa.CONCLUIDA));
    }

    // 🚫 Tarefa não encontrada
    @Test
    void deveLancarExcecaoQuandoTarefaNaoExistirAoCriarSubtarefa() {
        // Given
        Subtarefa subtarefa = new Subtarefa();
        subtarefa.setTitulo("Subtarefa teste");

        when(tarefaRepository.findById("tarefa-999")).thenReturn(Optional.empty());

        // When / Then
        assertThrows(TarefaNotFoundException.class, () ->
                subtarefaService.criarSubtarefa("tarefa-999", subtarefa));
    }

    // ✅ Filtra subtarefas
    @Test
    void deveFiltrarSubtarefasPorStatusETarefa() {
        // Given
        String tarefaId = "tarefa-1";
        StatusTarefa status = StatusTarefa.PENDENTE;
        Pageable pageable = PageRequest.of(0, 10);

        Subtarefa s1 = new Subtarefa();
        s1.setId("sub-1");
        s1.setTitulo("Subtarefa 1");
        s1.setStatus(StatusTarefa.PENDENTE);

        Subtarefa s2 = new Subtarefa();
        s2.setId("sub-2");
        s2.setTitulo("Subtarefa 2");
        s2.setStatus(StatusTarefa.PENDENTE);

        List<Subtarefa> lista = List.of(s1, s2);
        Page<Subtarefa> page = new PageImpl<>(lista);

        // Mock do repository
        Mockito.when(subtarefaRepository.findByTarefaIdAndStatus(
                tarefaId, status, pageable)).thenReturn(page);

        // When
        Page<Subtarefa> result = subtarefaService.listarSubtarefas(tarefaId, status, pageable);

        // Then
        assertThat(result.getContent()).hasSize(2);
    }
}
