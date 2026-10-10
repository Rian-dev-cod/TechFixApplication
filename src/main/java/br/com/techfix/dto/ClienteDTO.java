package br.com.techfix.dto;

import br.com.techfix.domain.Cliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public final class ClienteDTO {

    private ClienteDTO() {
    }

    public record Request(
            @NotBlank @Size(max = 120) String nome,
            @NotBlank @Size(max = 20) String telefone,
            @Email @Size(max = 120) String email
    ) {
    }

    public record Response(Long id, String nome, String telefone, String email, LocalDateTime criadoEm) {

        public static Response de(Cliente cliente) {
            return new Response(cliente.getId(), cliente.getNome(), cliente.getTelefone(),
                    cliente.getEmail(), cliente.getCriadoEm());
        }
    }
}
