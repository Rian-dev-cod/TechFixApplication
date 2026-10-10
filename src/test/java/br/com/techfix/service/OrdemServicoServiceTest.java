package br.com.techfix.service;

import br.com.techfix.domain.*;
import br.com.techfix.dto.OrdemServicoDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.repository.OrdemServicoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdemServicoServiceTest {

    private static final LocalDate HOJE = LocalDate.of(2026, 10, 10);

    @Mock
    private OrdemServicoRepository repository;

    @Mock
    private EquipamentoService equipamentoService;

    private OrdemServicoService service;
    private Equipamento equipamento;

    @BeforeEach
    void setUp() {
        Clock relogioFixo = Clock.fixed(HOJE.atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        service = new OrdemServicoService(repository, equipamentoService, relogioFixo);

        var cliente = new Cliente("João", "62999990000", null);
        equipamento = new Equipamento(TipoEquipamento.IMPRESSORA, "Epson", "L3250", "X123", cliente);
    }

    @Test
    @DisplayName("Nova OS deve começar com status RECEBIDO")
    void abrirComStatusRecebido() {
        when(equipamentoService.buscarEntidade(1L)).thenReturn(equipamento);
        when(repository.save(any(OrdemServico.class))).thenAnswer(inv -> inv.getArgument(0));

        var resposta = service.abrir(new OrdemServicoDTO.AbrirRequest(1L, "Não puxa papel", HOJE.plusDays(3)));

        assertThat(resposta.status()).isEqualTo(StatusOrdemServico.RECEBIDO);
        assertThat(resposta.defeitoRelatado()).isEqualTo("Não puxa papel");
        assertThat(resposta.clienteNome()).isEqualTo("João");
    }

    @Test
    @DisplayName("Não deve abrir OS para equipamento inexistente")
    void abrirComEquipamentoInexistente() {
        when(equipamentoService.buscarEntidade(9L)).thenThrow(new RecursoNaoEncontradoException("Equipamento", 9L));

        assertThatThrownBy(() -> service.abrir(new OrdemServicoDTO.AbrirRequest(9L, "Defeito", null)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve registrar diagnóstico e valor do orçamento")
    void registrarOrcamento() {
        var os = new OrdemServico(equipamento, "Não puxa papel", null);
        when(repository.findById(1L)).thenReturn(Optional.of(os));

        var resposta = service.registrarOrcamento(1L,
                new OrdemServicoDTO.OrcamentoRequest("Rolete de tração gasto", new BigDecimal("150.00")));

        assertThat(resposta.diagnostico()).isEqualTo("Rolete de tração gasto");
        assertThat(resposta.valorOrcamento()).isEqualByComparingTo("150");
    }

    @Test
    @DisplayName("Ao marcar como ENTREGUE deve registrar a data de entrega")
    void entregarRegistraData() {
        var os = new OrdemServico(equipamento, "Não puxa papel", null);
        when(repository.findById(1L)).thenReturn(Optional.of(os));

        var resposta = service.alterarStatus(1L, StatusOrdemServico.ENTREGUE);

        assertThat(resposta.status()).isEqualTo(StatusOrdemServico.ENTREGUE);
        assertThat(resposta.dataEntrega()).isNotNull();
    }

    @Test
    @DisplayName("Deve buscar atrasadas usando a data de hoje")
    void atrasadasUsaDataAtual() {
        when(repository.buscarAtrasadas(HOJE)).thenReturn(List.of());

        service.listarAtrasadas();

        verify(repository).buscarAtrasadas(HOJE);
    }
}
