package br.com.techfix.repository;

import br.com.techfix.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
