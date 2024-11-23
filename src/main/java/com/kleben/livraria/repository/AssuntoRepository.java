package com.kleben.livraria.repository;

import com.kleben.livraria.model.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Integer> {
    Assunto findByDescricaoAssuntoIgnoreCase(String descricaoAssunto);

    boolean existsByCodAssunto(Integer codAssunto);
}
