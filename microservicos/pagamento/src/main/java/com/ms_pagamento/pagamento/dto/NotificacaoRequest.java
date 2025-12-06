package com.ms_pagamento.pagamento.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class NotificacaoRequest {
    private Long usuarioId;
    private String tipo;
    private String titulo;
    private String mensagem;
    private String dados;

    public NotificacaoRequest() {}

    public NotificacaoRequest(Long usuarioId, String tipo, String titulo, String mensagem, String dados) {
        this.usuarioId = usuarioId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dados = dados;
    }

    // Getters e Setters
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
}
