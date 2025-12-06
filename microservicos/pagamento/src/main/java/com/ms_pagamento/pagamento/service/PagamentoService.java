package com.ms_pagamento.pagamento.service;

import com.ms_pagamento.pagamento.client.NotificacaoClient;
import com.ms_pagamento.pagamento.dto.NotificacaoRequest;
import com.ms_pagamento.pagamento.model.PagamentoModel;
import com.ms_pagamento.pagamento.repository.PagamentoRepository;
import com.ms_pagamento.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {
    
    @Autowired
    private PagamentoRepository pagamentoRepository;
    
    @Autowired
    private NotificacaoClient notificacaoClient;

    public List<PagamentoModel> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Optional<PagamentoModel> buscarPorId(Long id) {
        return pagamentoRepository.findById(id);
    }

    public PagamentoModel salvar(PagamentoModel pagamento) {
        // Define data e status se não foram definidos
        if (pagamento.getData() == null) {
            pagamento.setData(LocalDateTime.now());
        }
        if (pagamento.getStatus() == null) {
            pagamento.setStatus(PaymentStatus.APROVED);
        }
        
        // Salva o pagamento
        PagamentoModel salvo = pagamentoRepository.save(pagamento);
        
        // Envia notificação
        try {
            NotificacaoRequest notificacao = new NotificacaoRequest(
                salvo.getUsuarioId(),
                "PAGAMENTO",
                "Pagamento Confirmado",
                String.format("Seu pagamento de R$ %.2f foi confirmado com sucesso!", salvo.getValor()),
                String.format("{\"pagamentoId\": %d, \"livroId\": %d, \"valor\": %.2f, \"status\": \"%s\"}", 
                    salvo.getId(), salvo.getLivroId(), salvo.getValor(), salvo.getStatus())
            );
            notificacaoClient.enviarNotificacao(notificacao);
        } catch (Exception e) {
            System.err.println("Falha ao enviar notificação: " + e.getMessage());
        }
        
        return salvo;
    }

    public void deletar(Long id) {
        pagamentoRepository.deleteById(id);
    }
}
