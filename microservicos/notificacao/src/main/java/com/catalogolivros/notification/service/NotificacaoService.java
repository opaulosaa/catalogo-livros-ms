package com.catalogolivros.notification.service;

import com.catalogolivros.notification.client.UsuarioClient;
import com.catalogolivros.notification.dto.UsuarioDTO;
import com.catalogolivros.notification.model.Notificacao;
import com.catalogolivros.notification.repository.NotificacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

    private final NotificacaoRepository repository;
    private final UsuarioClient usuarioClient;
    private static final int MAX_TENTATIVAS = 3;
    private static final int JANELA_DEDUPLICACAO_MINUTOS = 5;

    public NotificacaoService(NotificacaoRepository repository, UsuarioClient usuarioClient) {
        this.repository = repository;
        this.usuarioClient = usuarioClient;
    }

    public Notificacao criar(Notificacao notificacao) {
        // Validar usuário
        try {
            UsuarioDTO usuario = usuarioClient.buscarPorId(notificacao.getUsuarioId());
            if (usuario == null) {
                throw new RuntimeException("Usuário não encontrado");
            }
        } catch (Exception e) {
            System.err.println("Erro ao validar usuário: " + e.getMessage());
        }

        // Verificar deduplicação
        LocalDateTime limiteDeduplicacao = LocalDateTime.now().minusMinutes(JANELA_DEDUPLICACAO_MINUTOS);
        List<Notificacao> notificacoesRecentes = repository.findByUsuarioIdAndTipoAndDataCriacaoAfter(
            notificacao.getUsuarioId(),
            notificacao.getTipo(),
            limiteDeduplicacao
        );

        // Verificar se já existe notificação similar
        for (Notificacao existente : notificacoesRecentes) {
            if (existente.getTitulo().equals(notificacao.getTitulo()) &&
                existente.getMensagem().equals(notificacao.getMensagem())) {
                System.out.println("Notificação duplicada detectada, retornando existente");
                return existente;
            }
        }

        // Salvar notificação
        Notificacao salva = repository.save(notificacao);

        // Tentar envio
        tentarEnvio(salva);

        return salva;
    }

    public Notificacao buscarPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Notificacao> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    public List<Notificacao> listarTodas() {
        return repository.findAll();
    }

    public void processarWebhook(Notificacao notificacao) {
        criar(notificacao);
    }

    private void tentarEnvio(Notificacao notificacao) {
        try {
            // Buscar informações do usuário para envio
            UsuarioDTO usuario = usuarioClient.buscarPorId(notificacao.getUsuarioId());
            
            // Simular envio de e-mail (aqui você integraria com SMTP ou provider)
            System.out.println("=== ENVIANDO NOTIFICAÇÃO ===");
            System.out.println("Para: " + (usuario != null ? usuario.getEmail() : "email@desconhecido.com"));
            System.out.println("Usuário: " + (usuario != null ? usuario.getNome() : "Desconhecido"));
            System.out.println("Tipo: " + notificacao.getTipo());
            System.out.println("Título: " + notificacao.getTitulo());
            System.out.println("Mensagem: " + notificacao.getMensagem());
            System.out.println("Dados: " + notificacao.getDados());
            System.out.println("============================");

            // Marcar como enviada
            notificacao.setStatus("ENVIADA");
            notificacao.setTentativas(notificacao.getTentativas() + 1);
            repository.save(notificacao);

        } catch (Exception e) {
            // Incrementar tentativas
            notificacao.setTentativas(notificacao.getTentativas() + 1);
            
            if (notificacao.getTentativas() >= MAX_TENTATIVAS) {
                notificacao.setStatus("FALHA");
                System.err.println("Falha ao enviar notificação após " + MAX_TENTATIVAS + " tentativas");
            } else {
                notificacao.setStatus("PENDENTE");
                System.err.println("Erro ao enviar notificação (tentativa " + notificacao.getTentativas() + "): " + e.getMessage());
            }
            
            repository.save(notificacao);
        }
    }

    public void reprocessarPendentes() {
        List<Notificacao> pendentes = repository.findByStatus("PENDENTE");
        for (Notificacao notificacao : pendentes) {
            if (notificacao.getTentativas() < MAX_TENTATIVAS) {
                tentarEnvio(notificacao);
            }
        }
    }
}
