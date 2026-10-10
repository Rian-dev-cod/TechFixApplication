package br.com.techfix.dto;

import br.com.techfix.domain.Equipamento;
import br.com.techfix.domain.TipoEquipamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public final class EquipamentoDTO {

    private EquipamentoDTO() {
    }

    public record Request(
            @NotNull TipoEquipamento tipo,
            @NotBlank @Size(max = 60) String marca,
            @NotBlank @Size(max = 60) String modelo,
            @Size(max = 60) String numeroSerie,
            @NotNull Long clienteId
    ) {
    }

    public record Response(Long id, TipoEquipamento tipo, String marca, String modelo,
                           String numeroSerie, Long clienteId, String clienteNome) {

        public static Response de(Equipamento e) {
            return new Response(e.getId(), e.getTipo(), e.getMarca(), e.getModelo(), e.getNumeroSerie(),
                    e.getCliente().getId(), e.getCliente().getNome());
        }
    }
}
