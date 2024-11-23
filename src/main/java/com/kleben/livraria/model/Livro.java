package com.kleben.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "livro")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "livro_seq")
    @SequenceGenerator(name = "livro_seq", sequenceName = "livro_seq", allocationSize = 1)
    @Column(name = "cod_livro")
    private Integer codLivro;

    @Column(name = "titulo_livro", nullable = false, length = 40)
    private String tituloLivro;

    @Column(name = "editora_livro", nullable = false, length = 40)
    private String editoraLivro;

    @Column(name = "edicao_livro", nullable = false)
    private Integer edicaoLivro;

    @Column(name = "ano_publicacao_livro", nullable = false, length = 4)
    private String anoPublicacaoLivro;

    @Column(name = "preco", nullable = false, precision = 10, scale = 2)
    private BigDecimal preco;

    @ManyToMany
    @JoinTable(
        name = "livro_autor",
        joinColumns = @JoinColumn(name = "cod_livro"),
        inverseJoinColumns = @JoinColumn(name = "cod_autor")
    )
    private Set<Autor> listAutor = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "livro_assunto",
        joinColumns = @JoinColumn(name = "cod_livro"),
        inverseJoinColumns = @JoinColumn(name = "cod_assunto")
    )
    private Set<Assunto> listAssunto = new HashSet<>();
}
