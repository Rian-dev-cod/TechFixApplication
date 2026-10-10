package br.com.techfix.controller;

import br.com.techfix.dto.ClienteDTO;
import br.com.techfix.dto.EquipamentoDTO;
import br.com.techfix.service.ClienteService;
import br.com.techfix.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService service;
    private final EquipamentoService equipamentoService;

    public ClienteController(ClienteService service, EquipamentoService equipamentoService) {
        this.service = service;
        this.equipamentoService = equipamentoService;
    }

    @PostMapping
    @Operation(summary = "Cadastra um cliente")
    public ResponseEntity<ClienteDTO.Response> criar(@RequestBody @Valid ClienteDTO.Request dados) {
        var criado = service.criar(dados);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping
    @Operation(summary = "Lista clientes (opcionalmente filtrando por nome)")
    public Page<ClienteDTO.Response> listar(@RequestParam(required = false) String nome,
                                            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return service.listar(nome, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cliente pelo id")
    public ClienteDTO.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza os dados de um cliente")
    public ClienteDTO.Response atualizar(@PathVariable Long id, @RequestBody @Valid ClienteDTO.Request dados) {
        return service.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cliente")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/equipamentos")
    @Operation(summary = "Lista os equipamentos de um cliente")
    public List<EquipamentoDTO.Response> equipamentos(@PathVariable Long id) {
        return equipamentoService.listarPorCliente(id);
    }
}
