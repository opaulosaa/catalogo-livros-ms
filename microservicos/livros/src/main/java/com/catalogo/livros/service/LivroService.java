package com.catalogo.livros.service;

import com.catalogo.livros.model.Livro;
import com.catalogo.livros.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LivroService {

    private final LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public Livro salvar(Livro livro) {
        if (livro == null) {
            throw new IllegalArgumentException("Livro não pode ser nulo");
        }
        return repository.save(livro);
    }

    public List<Livro> listar() {
        return repository.findAll();
    }

    public Livro buscarPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        return repository.findById(id).orElse(null);
    }

    public void deletar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo");
        }
        repository.deleteById(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {
        return repository.findByTituloContaining(titulo);
    }

    public List<Livro> buscarPorCategoria(String categoria) {
        return repository.findByCategoria(categoria);
    }
}