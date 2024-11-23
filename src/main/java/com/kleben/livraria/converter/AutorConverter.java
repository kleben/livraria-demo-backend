package com.kleben.livraria.converter;

import com.kleben.livraria.dto.AutorDTO;
import com.kleben.livraria.model.Autor;
import org.springframework.stereotype.Component;

@Component
public class AutorConverter {

    // Converte de AutorDTO para Autor
    public Autor toEntity(AutorDTO dto) {
        Autor autor = new Autor();
        autor.setCodAutor(dto.getCodAutor()); // Caso necessário para edições
        autor.setNomeAutor(dto.getNomeAutor());
        return autor;
    }

    // Converte de Autor para AutorDTO (caso precise retornar para o frontend)
    public AutorDTO toDto(Autor entity) {
        AutorDTO dto = new AutorDTO();
        dto.setCodAutor(entity.getCodAutor());
        dto.setNomeAutor(entity.getNomeAutor());
        return dto;
    }
}
