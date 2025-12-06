package com.catalogo.usuarios.controller;

import com.catalogo.usuarios.model.Usuario;
import com.catalogo.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user") // Base Path conforme PDF
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    // 3.1 Criar Usuário
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody @Valid Usuario usuario) {
        try {
            Usuario novoUsuario = service.criar(usuario);
            // Retorno 201 Created conforme PDF
            return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
        } catch (IllegalArgumentException e) {
            // Retorno 400 Bad Request se email duplicado
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 3.2 Listar Todos os Usuários
    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        // Retorno 200 OK
        return ResponseEntity.ok(service.listarTodos());
    }

    // 3.3 Buscar Usuário por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Usuario usuario = service.buscarPorId(id);
        if (usuario != null) {
            // 200 OK se encontrado
            return ResponseEntity.ok(usuario);
        }
        // 404 Not Found se não existir
        return ResponseEntity.notFound().build();
    }
}