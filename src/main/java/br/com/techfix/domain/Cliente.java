package br.com.techfix.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 20)
    private String telefone;

    @Column(length = 120)
    private String email;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    protected Cliente() {
        // exigido pelo JPA
    }

    public Cliente(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    @PrePersist
    void aoCriar() {
        this.criadoEm = LocalDateTime.now();
    }

    public void atualizar(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getTelefone() { return telefone; }
    public String getEmail() { return email; }
    public LocalDateTime getCriadoEm() { return criadoEm; }
}
