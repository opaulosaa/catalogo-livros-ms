package com.catalogo.livros.dto;

public class PagamentoResponse {
    private Long id;
    private String status;
    private String mensagem;

    public PagamentoResponse() {}

    public PagamentoResponse(Long id, String status, String mensagem) {
        this.id = id;
        this.status = status;
        this.mensagem = mensagem;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}
