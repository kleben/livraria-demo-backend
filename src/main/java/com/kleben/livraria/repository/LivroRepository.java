package com.kleben.livraria.repository;

import com.kleben.livraria.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Integer> {
    @Query(value = "SELECT * FROM livro_detalhes", nativeQuery = true)
    List<Livro> findAllLivroDetalhes();

    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM Livro l JOIN l.listAssunto a WHERE a.codAssunto = :codAssunto")
    boolean existsByCodAssunto(Integer codAssunto);

    boolean existsByTituloLivro(String tituloLivro);
}
