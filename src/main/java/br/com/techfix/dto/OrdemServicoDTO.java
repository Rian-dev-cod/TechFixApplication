package br.com.techfix.dto;

import br.com.techfix.domain.OrdemServico;
import br.com.techfix.domain.StatusOrdemServico;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public final class OrdemServicoDTO {

    private OrdemServicoDTO() {
    }

    public record AbrirRequest(
            @NotNull Long equipamentoId,
            @NotBlank @Size(max = 500) String defeitoRelatado,
            @FutureOrPresent LocalDate previsaoEntrega
    ) {
    }

    public record OrcamentoRequest(
            @NotBlank @Size(max = 1000) String diagnostico,
            @NotNull @PositiveOrZero BigDecimal valorOrcamento
    ) {
    }

    public record StatusRequest(@NotNull StatusOrdemServico status) {
    }

    public record Response(
            Long numero,
            StatusOrdemServico status,
            String defeitoRelatado,
            String diagnostico,
            BigDecimal valorOrcamento,
            LocalDateTime dataEntrada,
            LocalDate previsaoEntrega,
            LocalDateTime dataEntrega,
            Long equipamentoId,
            String equipamento,
            Long clienteId,
            String clienteNome
    ) {
        public static Response de(OrdemServico os) {
            var eq = os.getEquipamento();
            var cliente = eq.getCliente();
            return new Response(
                    os.getId(), os.getStatus(), os.getDefeitoRelatado(), os.getDiagnostico(),
                    os.getValorOrcamento(), os.getDataEntrada(), os.getPrevisaoEntrega(), os.getDataEntrega(),
                    eq.getId(), eq.getTipo() + " " + eq.getMarca() + " " + eq.getModelo(),
                    cliente.getId(), cliente.getNome());
        }
    }
}
