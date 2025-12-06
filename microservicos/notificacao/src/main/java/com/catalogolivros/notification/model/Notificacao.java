package com.catalogolivros.notification.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioId;
    private String tipo; // PAGAMENTO, PEDIDO, SISTEMA
    private String titulo;
    
    @Column(length = 1000)
    private String mensagem;
    
    @Column(length = 2000)
    private String dados;
    
    private String status; // PENDENTE, ENVIADA, FALHA
    private Integer tentativas;
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
        if (this.status == null) {
            this.status = "PENDENTE";
        }
        if (this.tentativas == null) {
            this.tentativas = 0;
        }
    }

    // ===== GETTERS E SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }

    public String getDados() { return dados; }
    public void setDados(String dados) { this.dados = dados; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Integer getTentativas() { return tentativas; }
    public void setTentativas(Integer tentativas) { this.tentativas = tentativas; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
