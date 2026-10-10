package br.com.techfix.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "ordem_servico")
public class OrdemServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Column(name = "defeito_relatado", nullable = false, length = 500)
    private String defeitoRelatado;

    @Column(length = 1000)
    private String diagnostico;

    @Column(name = "valor_orcamento", precision = 10, scale = 2)
    private BigDecimal valorOrcamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusOrdemServico status;

    @Column(name = "data_entrada", nullable = false, updatable = false)
    private LocalDateTime dataEntrada;

    @Column(name = "previsao_entrega")
    private LocalDate previsaoEntrega;

    @Column(name = "data_entrega")
    private LocalDateTime dataEntrega;

    protected OrdemServico() {
    }

    public OrdemServico(Equipamento equipamento, String defeitoRelatado, LocalDate previsaoEntrega) {
        this.equipamento = equipamento;
        this.defeitoRelatado = defeitoRelatado;
        this.previsaoEntrega = previsaoEntrega;
        this.status = StatusOrdemServico.RECEBIDO;
    }

    @PrePersist
    void aoCriar() {
        this.dataEntrada = LocalDateTime.now();
    }

    public void registrarOrcamento(String diagnostico, BigDecimal valorOrcamento) {
        this.diagnostico = diagnostico;
        this.valorOrcamento = valorOrcamento;
    }

    public void alterarStatus(StatusOrdemServico novoStatus) {
        this.status = novoStatus;
        if (novoStatus == StatusOrdemServico.ENTREGUE) {
            this.dataEntrega = LocalDateTime.now();
        }
    }

    public Long getId() { return id; }
    public Equipamento getEquipamento() { return equipamento; }
    public String getDefeitoRelatado() { return defeitoRelatado; }
    public String getDiagnostico() { return diagnostico; }
    public BigDecimal getValorOrcamento() { return valorOrcamento; }
    public StatusOrdemServico getStatus() { return status; }
    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public LocalDate getPrevisaoEntrega() { return previsaoEntrega; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }
}
