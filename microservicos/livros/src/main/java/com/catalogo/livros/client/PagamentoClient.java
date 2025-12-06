package com.catalogo.livros.client;

import com.catalogo.livros.dto.PagamentoRequest;
import com.catalogo.livros.dto.PagamentoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PagamentoClient {

    private final RestTemplate restTemplate;
    private final String pagamentoServiceUrl;

    public PagamentoClient(RestTemplate restTemplate,
                          @Value("${pagamento.service.url}") String pagamentoServiceUrl) {
        this.restTemplate = restTemplate;
        this.pagamentoServiceUrl = pagamentoServiceUrl;
    }

    public PagamentoResponse processarPagamento(PagamentoRequest request) {
        try {
            String url = pagamentoServiceUrl + "/pagamentos";
            return restTemplate.postForObject(url, request, PagamentoResponse.class);
        } catch (Exception e) {
            System.err.println("Erro ao processar pagamento: " + e.getMessage());
            return new PagamentoResponse(null, "ERRO", "Falha ao processar pagamento: " + e.getMessage());
        }
    }
}
