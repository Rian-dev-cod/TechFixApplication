package br.com.techfix.service;

import br.com.techfix.domain.Cliente;
import br.com.techfix.dto.ClienteDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.repository.ClienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    @DisplayName("Deve salvar o cliente com os dados recebidos")
    void criar() {
        when(repository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        var resposta = service.criar(new ClienteDTO.Request("Maria Souza", "62999990000", "maria@email.com"));

        assertThat(resposta.nome()).isEqualTo("Maria Souza");
        assertThat(resposta.telefone()).isEqualTo("62999990000");
        verify(repository).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Deve atualizar os dados de um cliente existente")
    void atualizar() {
        var cliente = new Cliente("Maria", "62999990000", null);
        when(repository.findById(1L)).thenReturn(Optional.of(cliente));

        var resposta = service.atualizar(1L, new ClienteDTO.Request("Maria Souza", "62988880000", "m@email.com"));

        assertThat(resposta.nome()).isEqualTo("Maria Souza");
        assertThat(resposta.telefone()).isEqualTo("62988880000");
        assertThat(resposta.email()).isEqualTo("m@email.com");
    }

    @Test
    @DisplayName("Deve lançar exceção quando o cliente não existe")
    void buscarInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.buscar(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Não deve excluir quando o cliente não existe")
    void excluirInexistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.excluir(99L)).isInstanceOf(RecursoNaoEncontradoException.class);
        verify(repository, never()).delete(any());
    }
}
