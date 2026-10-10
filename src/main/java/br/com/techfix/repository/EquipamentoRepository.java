package br.com.techfix.repository;

import br.com.techfix.domain.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    List<Equipamento> findByClienteId(Long clienteId);
}
