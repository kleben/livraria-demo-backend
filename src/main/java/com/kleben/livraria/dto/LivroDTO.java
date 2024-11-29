package com.kleben.livraria.dto;

import jakarta.validation.constraints.*;
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

    @NotBlank(message = "O título do livro é obrigatório.")
    private String tituloLivro;

    @NotBlank(message = "O nome da editora é obrigatório.")
    private String editoraLivro;

    @Min(value = 1, message = "A edição do livro deve ser pelo menos 1.")
    private Integer edicaoLivro;

    @Pattern(
            regexp = "^(19|20)\\d{2}$",
            message = "O ano de publicação deve estar no formato YYYY e ser válido."
    )
    private String anoPublicacaoLivro;

    @NotNull(message = "O preço do livro é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero.")
    private BigDecimal preco;

    @NotNull(message = "A lista de autores não pode ser nula.")
    @Size(min = 1, message = "O livro deve ter pelo menos um autor.")
    private Set<AutorDTO> listAutor;

    @NotNull(message = "A lista de assuntos não pode ser nula.")
    @Size(min = 1, message = "O livro deve ter pelo menos um assunto.")
    private Set<AssuntoDTO> listAssunto;
}
