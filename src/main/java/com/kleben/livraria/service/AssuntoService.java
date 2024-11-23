package com.kleben.livraria.service;

import com.kleben.livraria.model.Assunto;
import com.kleben.livraria.repository.AssuntoRepository;
import com.kleben.livraria.repository.LivroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AssuntoService {

    private final AssuntoRepository assuntoRepository;
    private final LivroRepository livroRepository;

    public AssuntoService(AssuntoRepository assuntoRepository, LivroRepository livroRepository) {
        this.assuntoRepository = assuntoRepository;
        this.livroRepository = livroRepository;
    }

    public Optional<Assunto> buscarPorId(Integer id) {
        return assuntoRepository.findById(id);
    }
    public Assunto salvar(Assunto assunto) {
        return assuntoRepository.save(assunto);
    }

    public Page<Assunto> findAll(PageRequest pageRequest) {
        PageRequest sortedPageRequest = PageRequest.of(
                pageRequest.getPageNumber(),
                pageRequest.getPageSize(),
                Sort.by(Sort.Direction.ASC, "descricaoAssunto")
        );
        return assuntoRepository.findAll(sortedPageRequest);
    }

    public Assunto findByDescricao(String descricaoAssunto) {
        return assuntoRepository.findByDescricaoAssuntoIgnoreCase(descricaoAssunto);
    }

    public boolean existePorId(Integer id) {
        return assuntoRepository.existsByCodAssunto(id);
    }

    public void deletarPorId(Integer id) {
        assuntoRepository.deleteById(id);
    }

    public boolean estaAssociadoALivro(Integer codAssunto) {
        return livroRepository.existsByCodAssunto(codAssunto);
    }
}
