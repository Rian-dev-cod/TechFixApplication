package br.com.techfix.repository;

import br.com.techfix.domain.OrdemServico;
import br.com.techfix.domain.StatusOrdemServico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {

    Page<OrdemServico> findByStatus(StatusOrdemServico status, Pageable pageable);

    @Query(value = """
            SELECT os.*
              FROM ordem_servico os
             WHERE os.previsao_entrega < :hoje
               AND os.status NOT IN ('ENTREGUE', 'CANCELADO')
             ORDER BY os.previsao_entrega
            """, nativeQuery = true)
    List<OrdemServico> buscarAtrasadas(LocalDate hoje);
}
