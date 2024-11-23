package com.kleben.livraria.converter;

import com.kleben.livraria.dto.AssuntoDTO;
import com.kleben.livraria.model.Assunto;
import org.springframework.stereotype.Component;

@Component
public class AssuntoConverter {

    public Assunto toEntity(AssuntoDTO dto) {
        Assunto assunto = new Assunto();
        assunto.setCodAssunto(dto.getCodAssunto());
        assunto.setDescricaoAssunto(dto.getDescricaoAssunto());
        return assunto;
    }

    public AssuntoDTO toDto(Assunto entity) {
        AssuntoDTO dto = new AssuntoDTO();
        dto.setCodAssunto(entity.getCodAssunto());
        dto.setDescricaoAssunto(entity.getDescricaoAssunto());
        return dto;
    }

}