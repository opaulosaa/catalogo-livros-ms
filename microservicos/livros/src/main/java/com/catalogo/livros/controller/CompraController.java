package com.catalogo.livros.controller;

import com.catalogo.livros.client.PagamentoClient;
import com.catalogo.livros.dto.CompraRequest;
import com.catalogo.livros.dto.PagamentoRequest;
import com.catalogo.livros.dto.PagamentoResponse;
import com.catalogo.livros.model.Livro;
import com.catalogo.livros.service.LivroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/compras")
@CrossOrigin(origins = "*")
public class CompraController {

    @Autowired
    private LivroService livroService;

    @Autowired
    private PagamentoClient pagamentoClient;

    @PostMapping
    public ResponseEntity<Map<String, Object>> realizarCompra(@RequestBody CompraRequest compraRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar se o livro existe
            Livro livro = livroService.buscarPorId(compraRequest.getLivroId());
            if (livro == null) {
                response.put("sucesso", false);
                response.put("mensagem", "Livro não encontrado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Criar requisição de pagamento
            PagamentoRequest pagamentoRequest = new PagamentoRequest(
                compraRequest.getUsuarioId(),
                compraRequest.getLivroId(),
                compraRequest.getValor(),
                compraRequest.getMeioPagamento()
            );

            // Processar pagamento
            PagamentoResponse pagamentoResponse = pagamentoClient.processarPagamento(pagamentoRequest);

            if (pagamentoResponse != null && !"ERRO".equals(pagamentoResponse.getStatus())) {
                response.put("sucesso", true);
                response.put("mensagem", "Compra realizada com sucesso!");
                response.put("pagamentoId", pagamentoResponse.getId());
                response.put("livro", livro);
                return ResponseEntity.ok(response);
            } else {
                response.put("sucesso", false);
                response.put("mensagem", pagamentoResponse != null ? pagamentoResponse.getMensagem() : "Erro ao processar pagamento");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }

        } catch (Exception e) {
            response.put("sucesso", false);
            response.put("mensagem", "Erro ao processar compra: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
