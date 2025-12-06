package com.ms_pagamento.pagamento.client;

import com.ms_pagamento.pagamento.dto.NotificacaoRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class NotificacaoClient {

    private final RestTemplate restTemplate;
    private final String notificacaoServiceUrl;

    public NotificacaoClient(RestTemplate restTemplate,
                            @Value("${notificacao.service.url}") String notificacaoServiceUrl) {
        this.restTemplate = restTemplate;
        this.notificacaoServiceUrl = notificacaoServiceUrl;
    }

    public void enviarNotificacao(NotificacaoRequest request) {
        try {
            String url = notificacaoServiceUrl + "/notificacoes";
            restTemplate.postForEntity(url, request, String.class);
            System.out.println("Notificação enviada com sucesso para usuário: " + request.getUsuarioId());
        } catch (Exception e) {
            System.err.println("Erro ao enviar notificação: " + e.getMessage());
        }
    }
}
