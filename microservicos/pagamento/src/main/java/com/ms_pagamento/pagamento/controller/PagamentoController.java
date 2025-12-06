package com.ms_pagamento.pagamento.controller;

import com.ms_pagamento.pagamento.model.MeioPagamento;
import com.ms_pagamento.pagamento.model.PagamentoModel;
import com.ms_pagamento.pagamento.service.PagamentoService;
import com.ms_pagamento.status.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
@CrossOrigin(origins = "*")
public class PagamentoController {
    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping
    public List<PagamentoModel> listarTodos() {
        return pagamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<PagamentoModel> buscarPorId(@PathVariable Long id) {
        return pagamentoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> salvar(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            PagamentoModel pagamento = new PagamentoModel();
            pagamento.setUsuarioId(Long.parseLong(request.get("usuarioId").toString()));
            pagamento.setLivroId(Long.parseLong(request.get("livroId").toString()));
            pagamento.setValor(new java.math.BigDecimal(request.get("valor").toString()));
            pagamento.setMeioPagamento(MeioPagamento.valueOf(request.get("meioPagamento").toString()));
            pagamento.setData(LocalDateTime.now());
            pagamento.setStatus(PaymentStatus.APROVED);
            
            PagamentoModel salvo = pagamentoService.salvar(pagamento);
            
            response.put("id", salvo.getId());
            response.put("status", salvo.getStatus().toString());
            response.put("mensagem", "Pagamento processado com sucesso");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("id", null);
            response.put("status", "ERRO");
            response.put("mensagem", "Erro ao processar pagamento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
    }
}
