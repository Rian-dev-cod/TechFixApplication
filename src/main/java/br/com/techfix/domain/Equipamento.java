package br.com.techfix.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "equipamento")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoEquipamento tipo;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(nullable = false, length = 60)
    private String modelo;

    @Column(name = "numero_serie", length = 60)
    private String numeroSerie;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    protected Equipamento() {
    }

    public Equipamento(TipoEquipamento tipo, String marca, String modelo, String numeroSerie, Cliente cliente) {
        this.tipo = tipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.cliente = cliente;
    }

    public Long getId() { return id; }
    public TipoEquipamento getTipo() { return tipo; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getNumeroSerie() { return numeroSerie; }
    public Cliente getCliente() { return cliente; }
}
