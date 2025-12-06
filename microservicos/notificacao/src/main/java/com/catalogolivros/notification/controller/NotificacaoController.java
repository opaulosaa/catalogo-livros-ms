package com.catalogolivros.notification.controller;

import com.catalogolivros.notification.model.Notificacao;
import com.catalogolivros.notification.service.NotificacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@CrossOrigin(origins = "*")
public class NotificacaoController {

    private final NotificacaoService service;

    public NotificacaoController(NotificacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Notificacao> criar(@RequestBody Notificacao notificacao) {
        try {
            Notificacao criada = service.criar(notificacao);
            return ResponseEntity.ok(criada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacao> buscarPorId(@PathVariable Long id) {
        Notificacao notificacao = service.buscarPorId(id);
        if (notificacao != null) {
            return ResponseEntity.ok(notificacao);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Notificacao>> listarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuario(usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<Notificacao>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receberWebhook(@RequestBody Notificacao notificacao) {
        try {
            service.processarWebhook(notificacao);
            return ResponseEntity.ok("Webhook processado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar webhook: " + e.getMessage());
        }
    }

    @PostMapping("/reprocessar-pendentes")
    public ResponseEntity<String> reprocessarPendentes() {
        try {
            service.reprocessarPendentes();
            return ResponseEntity.ok("Notificações pendentes reprocessadas");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao reprocessar: " + e.getMessage());
        }
    }
}
