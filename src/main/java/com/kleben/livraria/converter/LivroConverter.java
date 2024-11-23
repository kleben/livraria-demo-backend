package com.kleben.livraria.converter;

import com.kleben.livraria.dto.AssuntoDTO;
import com.kleben.livraria.dto.AutorDTO;
import com.kleben.livraria.dto.LivroDTO;
import com.kleben.livraria.model.Livro;
import com.kleben.livraria.model.Autor;
import com.kleben.livraria.model.Assunto;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class LivroConverter {

    public Livro toEntity(LivroDTO dto) {
        Livro livro = new Livro();
        livro.setCodLivro(dto.getCodLivro());
        livro.setTituloLivro(dto.getTituloLivro());
        livro.setEditoraLivro(dto.getEditoraLivro());
        livro.setEdicaoLivro(dto.getEdicaoLivro());
        livro.setAnoPublicacaoLivro(dto.getAnoPublicacaoLivro());
        livro.setPreco(dto.getPreco());

        livro.setListAutor(dto.getListAutor().stream()
                .map(autorDTO -> new Autor(autorDTO.getCodAutor(), autorDTO.getNomeAutor()))
                .collect(Collectors.toSet()));

        livro.setListAssunto(dto.getListAssunto().stream()
                .map(assuntoDTO -> new Assunto(assuntoDTO.getCodAssunto(), assuntoDTO.getDescricaoAssunto()))
                .collect(Collectors.toSet()));

        return livro;
    }

    public LivroDTO toDto(Livro entity) {
        LivroDTO dto = new LivroDTO();
        dto.setCodLivro(entity.getCodLivro());
        dto.setTituloLivro(entity.getTituloLivro());
        dto.setEditoraLivro(entity.getEditoraLivro());
        dto.setEdicaoLivro(entity.getEdicaoLivro());
        dto.setAnoPublicacaoLivro(entity.getAnoPublicacaoLivro());
        dto.setPreco(entity.getPreco());

        dto.setListAutor(entity.getListAutor().stream()
                .map(autor -> new AutorDTO(autor.getCodAutor(), autor.getNomeAutor()))
                .collect(Collectors.toSet()));

        dto.setListAssunto(entity.getListAssunto().stream()
                .map(assunto -> new AssuntoDTO(assunto.getCodAssunto(), assunto.getDescricaoAssunto()))
                .collect(Collectors.toSet()));

        return dto;
    }

}