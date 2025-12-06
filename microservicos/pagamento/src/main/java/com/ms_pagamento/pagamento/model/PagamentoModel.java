package com.ms_pagamento.pagamento.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ms_pagamento.status.PaymentStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "pagamento")
public class PagamentoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MeioPagamento meioPagamento;
    
    @Column(nullable = false)
    private Long usuarioId;
    
    @Column(nullable = false)
    private Long livroId;

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public MeioPagamento getMeioPagamento() { return meioPagamento; }
    public void setMeioPagamento(MeioPagamento meioPagamento) { this.meioPagamento = meioPagamento; }
    
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
    
    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }
}
