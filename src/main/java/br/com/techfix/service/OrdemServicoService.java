package br.com.techfix.service;

import br.com.techfix.domain.OrdemServico;
import br.com.techfix.domain.StatusOrdemServico;
import br.com.techfix.dto.OrdemServicoDTO;
import br.com.techfix.exception.RecursoNaoEncontradoException;
import br.com.techfix.repository.OrdemServicoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrdemServicoService {

    private final OrdemServicoRepository repository;
    private final EquipamentoService equipamentoService;
    private final Clock clock;

    public OrdemServicoService(OrdemServicoRepository repository, EquipamentoService equipamentoService, Clock clock) {
        this.repository = repository;
        this.equipamentoService = equipamentoService;
        this.clock = clock;
    }

    @Transactional
    public OrdemServicoDTO.Response abrir(OrdemServicoDTO.AbrirRequest dados) {
        var equipamento = equipamentoService.buscarEntidade(dados.equipamentoId());
        var os = new OrdemServico(equipamento, dados.defeitoRelatado(), dados.previsaoEntrega());
        return OrdemServicoDTO.Response.de(repository.save(os));
    }

    @Transactional(readOnly = true)
    public Page<OrdemServicoDTO.Response> listar(StatusOrdemServico status, Pageable pageable) {
        Page<OrdemServico> pagina = (status == null)
                ? repository.findAll(pageable)
                : repository.findByStatus(status, pageable);
        return pagina.map(OrdemServicoDTO.Response::de);
    }

    @Transactional(readOnly = true)
    public OrdemServicoDTO.Response buscar(Long numero) {
        return OrdemServicoDTO.Response.de(buscarEntidade(numero));
    }

    @Transactional
    public OrdemServicoDTO.Response registrarOrcamento(Long numero, OrdemServicoDTO.OrcamentoRequest dados) {
        var os = buscarEntidade(numero);
        os.registrarOrcamento(dados.diagnostico(), dados.valorOrcamento());
        return OrdemServicoDTO.Response.de(os);
    }

    @Transactional
    public OrdemServicoDTO.Response alterarStatus(Long numero, StatusOrdemServico novoStatus) {
        var os = buscarEntidade(numero);
        os.alterarStatus(novoStatus);
        return OrdemServicoDTO.Response.de(os);
    }

    @Transactional(readOnly = true)
    public List<OrdemServicoDTO.Response> listarAtrasadas() {
        return repository.buscarAtrasadas(LocalDate.now(clock)).stream()
                .map(OrdemServicoDTO.Response::de)
                .toList();
    }

    private OrdemServico buscarEntidade(Long numero) {
        return repository.findById(numero)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ordem de serviço", numero));
    }
}
