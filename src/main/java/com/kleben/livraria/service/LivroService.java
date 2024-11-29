package com.kleben.livraria.service;

import com.kleben.livraria.converter.LivroConverter;
import com.kleben.livraria.dto.LivroDTO;
import com.kleben.livraria.exception.ValidationException;
import com.kleben.livraria.model.Livro;
import com.kleben.livraria.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    @Autowired
    private LivroConverter livroConverter;

    @Autowired
    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<Livro> buscarTodosLivros() {
        return livroRepository.findAll();
    }

    public Livro salvarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Livro cadastrar(LivroDTO livroDTO) {
//        if (Objects.isNull(livroDTO) || livroDTO.getTituloLivro().isBlank())
//            throw new ValidationException("O título do livro é obrigatório.");

        if (livroRepository.existsByTituloLivro(livroDTO.getTituloLivro().trim()))
            throw new ValidationException("Um livro com este título já existe.");

        Livro livro = livroConverter.toEntity(livroDTO);
        return livroRepository.save(livro);
    }
}
