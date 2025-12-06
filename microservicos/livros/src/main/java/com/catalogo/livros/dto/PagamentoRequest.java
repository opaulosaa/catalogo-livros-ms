package com.catalogo.livros.dto;

import java.math.BigDecimal;

public class PagamentoRequest {
    private Long usuarioId;
    private Long livroId;
    private BigDecimal valor;
    private String meioPagamento;

    public PagamentoRequest() {}

    public PagamentoRequest(Long usuarioId, Long livroId, BigDecimal valor, String meioPagamento) {
        this.usuarioId = usuarioId;
        this.livroId = livroId;
        this.valor = valor;
        this.meioPagamento = meioPagamento;
    }

    // Getters e Setters
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Long getLivroId() { return livroId; }
    public void setLivroId(Long livroId) { this.livroId = livroId; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getMeioPagamento() { return meioPagamento; }
    public void setMeioPagamento(String meioPagamento) { this.meioPagamento = meioPagamento; }
}
