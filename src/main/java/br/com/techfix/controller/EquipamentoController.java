package br.com.techfix.controller;

import br.com.techfix.dto.EquipamentoDTO;
import br.com.techfix.service.EquipamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/equipamentos")
@Tag(name = "Equipamentos")
public class EquipamentoController {

    private final EquipamentoService service;

    public EquipamentoController(EquipamentoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Cadastra um equipamento de um cliente")
    public ResponseEntity<EquipamentoDTO.Response> criar(@RequestBody @Valid EquipamentoDTO.Request dados) {
        var criado = service.criar(dados);
        var uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(uri).body(criado);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um equipamento pelo id")
    public EquipamentoDTO.Response buscar(@PathVariable Long id) {
        return service.buscar(id);
    }
}
