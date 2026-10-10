package br.com.techfix.service;

import br.com.techfix.domain.Cliente;
import br.com.techfix.dto.ClienteDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClienteDTO.Response criar(ClienteDTO.Request dados) {
        Cliente cliente = new Cliente(dados.nome(), dados.telefone(), dados.email());
        return ClienteDTO.Response.de(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public Page<ClienteDTO.Response> listar(String nome, Pageable pageable) {
        Page<Cliente> pagina = (nome == null || nome.isBlank())
                ? repository.findAll(pageable)
                : repository.findByNomeContainingIgnoreCase(nome, pageable);
        return pagina.map(ClienteDTO.Response::de);
    }

    @Transactional(readOnly = true)
    public ClienteDTO.Response buscar(Long id) {
        return ClienteDTO.Response.de(buscarEntidade(id));
    }

    @Transactional
    public ClienteDTO.Response atualizar(Long id, ClienteDTO.Request dados) {
        Cliente cliente = buscarEntidade(id);
        cliente.atualizar(dados.nome(), dados.telefone(), dados.email());
        return ClienteDTO.Response.de(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        repository.delete(buscarEntidade(id));
    }

    Cliente buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }
}
