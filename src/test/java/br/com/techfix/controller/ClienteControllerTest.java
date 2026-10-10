package br.com.techfix.controller;

import br.com.techfix.dto.ClienteDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.service.ClienteService;
import br.com.techfix.service.EquipamentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testa só a camada web (rotas, validação, códigos HTTP), com o service "mockado".
 */
@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ClienteService service;

    @MockitoBean
    private EquipamentoService equipamentoService;

    @Test
    @DisplayName("POST /api/clientes deve retornar 201 com Location")
    void criar() throws Exception {
        when(service.criar(any())).thenReturn(
                new ClienteDTO.Response(1L, "Maria", "62999990000", null, LocalDateTime.now()));

        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome": "Maria", "telefone": "62999990000"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/api/clientes/1"))
                .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    @DisplayName("POST /api/clientes sem nome deve retornar 400 indicando o campo")
    void criarInvalido() throws Exception {
        mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"telefone": "62999990000", "email": "invalido"}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.campos.nome").exists())
                .andExpect(jsonPath("$.campos.email").exists());
    }

    @Test
    @DisplayName("GET /api/clientes/{id} inexistente deve retornar 404")
    void buscarInexistente() throws Exception {
        when(service.buscar(99L)).thenThrow(new RecursoNaoEncontradoException("Cliente", 99L));

        mvc.perform(get("/api/clientes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Recurso não encontrado"));
    }
}
