package com.kleben.livraria.repository;

import com.kleben.livraria.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Integer> {
    Autor findByNomeAutor(String nomeAutor);

}