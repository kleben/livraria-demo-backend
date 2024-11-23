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
@Table(name = "assunto")
public class Assunto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assunto_seq")
    @SequenceGenerator(name = "assunto_seq", sequenceName = "assunto_seq", allocationSize = 1)
    @Column(name = "cod_assunto")
    private Integer codAssunto;

    @Column(name = "descricao_assunto", nullable = false, length = 20)
    private String descricaoAssunto;

}