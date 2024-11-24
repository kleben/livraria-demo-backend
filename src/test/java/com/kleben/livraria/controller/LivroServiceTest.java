package com.kleben.livraria.controller;

import com.kleben.livraria.converter.LivroConverter;
import com.kleben.livraria.dto.AssuntoDTO;
import com.kleben.livraria.dto.AutorDTO;
import com.kleben.livraria.dto.LivroDTO;
import com.kleben.livraria.model.Assunto;
import com.kleben.livraria.model.Autor;
import com.kleben.livraria.model.Livro;
import com.kleben.livraria.repository.LivroRepository;
import com.kleben.livraria.service.LivroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LivroServiceTest {

    @Mock
    private LivroRepository livroRepository;

    @Mock
    private LivroConverter livroConverter;

    @InjectMocks
    private LivroService livroService;

    private Livro livro;
    private LivroDTO livroDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Set<Autor> autores = new HashSet<>();
        autores.add(new Autor(1, "Autor Teste"));

        Set<Assunto> assuntos = new HashSet<>();
        assuntos.add(new Assunto(1, "Ficção"));

        livro = new Livro(1, "Livro Teste", "Editora Teste", 1, "2023", new BigDecimal("49.90"), autores, assuntos);

        Set<AutorDTO> autorDTOs = new HashSet<>();
        autorDTOs.add(new AutorDTO(1, "Autor Teste"));

        Set<AssuntoDTO> assuntoDTOs = new HashSet<>();
        assuntoDTOs.add(new AssuntoDTO(1, "Ficção"));

        livroDTO = new LivroDTO(1, "Livro Teste", "Editora Teste", 1, "2023", new BigDecimal("49.90"), autorDTOs, assuntoDTOs);
    }

    @Test
    public void deveSalvarLivroComSucesso() {
        when(livroRepository.save(any(Livro.class))).thenReturn(livro);

        Livro livroSalvo = livroService.salvarLivro(livro);

        assertThat(livroSalvo).isNotNull();
        assertThat(livroSalvo.getCodLivro()).isEqualTo(1);
        assertThat(livroSalvo.getTituloLivro()).isEqualTo("Livro Teste");
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    public void deveLancarExcecaoAoSalvarLivroComDadosInvalidos() {
        when(livroRepository.save(any(Livro.class))).thenThrow(DataIntegrityViolationException.class);

        assertThrows(DataIntegrityViolationException.class, () -> livroService.salvarLivro(livro));
        verify(livroRepository, times(1)).save(any(Livro.class));
    }

    @Test
    public void deveBuscarLivroPorIdComSucesso() {
        when(livroRepository.findById(1)).thenReturn(Optional.of(livro));

        Optional<Livro> livroEncontrado = livroRepository.findById(1);

        assertThat(livroEncontrado).isPresent();
        assertThat(livroEncontrado.get().getTituloLivro()).isEqualTo("Livro Teste");
        verify(livroRepository, times(1)).findById(1);
    }

    @Test
    public void deveRetornarVazioAoBuscarLivroInexistente() {
        when(livroRepository.findById(99)).thenReturn(Optional.empty());

        Optional<Livro> livroEncontrado = livroRepository.findById(99);

        assertThat(livroEncontrado).isNotPresent();
        verify(livroRepository, times(1)).findById(99);
    }

    @Test
    public void deveConverterLivroDTOParaEntidadeCorretamente() {
        when(livroConverter.toEntity(livroDTO)).thenReturn(livro);

        Livro livroConvertido = livroConverter.toEntity(livroDTO);

        assertThat(livroConvertido).isNotNull();
        assertThat(livroConvertido.getTituloLivro()).isEqualTo("Livro Teste");
        verify(livroConverter, times(1)).toEntity(livroDTO);
    }

    @Test
    public void deveConverterLivroParaDTOCorretamente() {
        when(livroConverter.toDto(livro)).thenReturn(livroDTO);

        LivroDTO livroDTOConvertido = livroConverter.toDto(livro);

        assertThat(livroDTOConvertido).isNotNull();
        assertThat(livroDTOConvertido.getTituloLivro()).isEqualTo("Livro Teste");
        verify(livroConverter, times(1)).toDto(livro);
    }
}