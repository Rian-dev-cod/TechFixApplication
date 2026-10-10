package br.com.techfix.service;

import br.com.techfix.domain.Equipamento;
import br.com.techfix.dto.EquipamentoDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.repository.EquipamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EquipamentoService {

    private final EquipamentoRepository repository;
    private final ClienteService clienteService;

    public EquipamentoService(EquipamentoRepository repository, ClienteService clienteService) {
        this.repository = repository;
        this.clienteService = clienteService;
    }

    @Transactional
    public EquipamentoDTO.Response criar(EquipamentoDTO.Request dados) {
        var cliente = clienteService.buscarEntidade(dados.clienteId());
        var equipamento = new Equipamento(dados.tipo(), dados.marca(), dados.modelo(), dados.numeroSerie(), cliente);
        return EquipamentoDTO.Response.de(repository.save(equipamento));
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO.Response> listarPorCliente(Long clienteId) {
        clienteService.buscarEntidade(clienteId); // garante 404 se o cliente não existir
        return repository.findByClienteId(clienteId).stream()
                .map(EquipamentoDTO.Response::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipamentoDTO.Response buscar(Long id) {
        return EquipamentoDTO.Response.de(buscarEntidade(id));
    }

    Equipamento buscarEntidade(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Equipamento", id));
    }
}
