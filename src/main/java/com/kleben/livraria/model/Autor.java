package com.kleben.livraria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "autor")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autor_seq")
    @SequenceGenerator(name = "autor_seq", sequenceName = "autor_seq", allocationSize = 1)
    @Column(name = "cod_autor")
    private Integer codAutor;

    @Column(name = "nome_autor", nullable = false, length = 40)
    private String nomeAutor;

}
