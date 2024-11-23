package com.kleben.livraria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {

    private Integer codLivro;
    private String tituloLivro;
    private String editoraLivro;
    private Integer edicaoLivro;
    private String anoPublicacaoLivro;
    private BigDecimal preco;
    private Set<AutorDTO> listAutor;
    private Set<AssuntoDTO> listAssunto;
}
