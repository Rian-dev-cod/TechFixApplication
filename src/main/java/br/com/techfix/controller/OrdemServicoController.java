package br.com.techfix.controller;

import br.com.techfix.domain.StatusOrdemServico;
import br.com.techfix.dto.OrdemServicoDTO;
import br.com.techfix.service.OrdemServicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/ordens-servico")
@Tag(name = "Ordens de Serviço")
public class OrdemServicoController {

    private final OrdemServicoService service;

    public OrdemServicoController(OrdemServicoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Abre uma nova OS (status inicial: RECEBIDO)")
    public ResponseEntity<OrdemServicoDTO.Response> abrir(@RequestBody @Valid OrdemServicoDTO.AbrirRequest dados) {
        var criada = service.abrir(dados);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{numero}").buildAndExpand(criada.numero()).toUri();
        return ResponseEntity.created(uri).body(criada);
    }

    @GetMapping
    @Operation(summary = "Lista OS (opcionalmente filtrando por status)")
    public Page<OrdemServicoDTO.Response> listar(
            @RequestParam(required = false) StatusOrdemServico status,
            @PageableDefault(size = 20, sort = "dataEntrada", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.listar(status, pageable);
    }

    @GetMapping("/atrasadas")
    @Operation(summary = "Lista OS com previsão de entrega vencida")
    public List<OrdemServicoDTO.Response> atrasadas() {
        return service.listarAtrasadas();
    }

    @GetMapping("/{numero}")
    @Operation(summary = "Consulta uma OS pelo número")
    public OrdemServicoDTO.Response buscar(@PathVariable Long numero) {
        return service.buscar(numero);
    }

    @PutMapping("/{numero}/orcamento")
    @Operation(summary = "Registra diagnóstico e valor do orçamento")
    public OrdemServicoDTO.Response orcamento(@PathVariable Long numero,
                                              @RequestBody @Valid OrdemServicoDTO.OrcamentoRequest dados) {
        return service.registrarOrcamento(numero, dados);
    }

    @PatchMapping("/{numero}/status")
    @Operation(summary = "Altera o status da OS")
    public OrdemServicoDTO.Response status(@PathVariable Long numero,
                                           @RequestBody @Valid OrdemServicoDTO.StatusRequest dados) {
        return service.alterarStatus(numero, dados.status());
    }
}
