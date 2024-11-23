package com.kleben.livraria.controller;

import com.kleben.livraria.converter.AutorConverter;
import com.kleben.livraria.converter.LivroConverter;
import com.kleben.livraria.dto.LivroDTO;
import com.kleben.livraria.exception.ErrorResponse;
import com.kleben.livraria.model.Livro;
import com.kleben.livraria.repository.LivroRepository;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.*;

@RestController
@RequestMapping("/livro")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private LivroConverter livroConverter;
    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    @GetMapping
    public ResponseEntity<Page<Livro>> listar(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size,
            @RequestParam(value = "sort", defaultValue = "tituloLivro", required = false) String sort,
            @RequestParam(value = "direction", defaultValue = "ASC", required = false) String direction) {
        try {
            logger.info("Buscando lista de livros");
            Sort.Direction sortDirection = Sort.Direction.fromString(direction);
            PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sort));
            Page<Livro> livros = livroRepository.findAll(pageRequest);
            return ResponseEntity.ok(livros);
        } catch (EmptyResultDataAccessException ex) {
            logger.error("Nenhum resultado encontrado para a consulta");
            return ResponseEntity.internalServerError().build();
        } catch (DataAccessException ex) {
            logger.error("Erro ao listar livros. Ocorreu um erro de acesso ao banco de dados");
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            logger.error("Erro ao listar livros: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Livro>> buscarPorId(@PathVariable Integer id) {
        try {
            logger.info("Buscando livro com ID: " + id);
            return ResponseEntity.ok(livroRepository.findById(id));
        } catch (Exception e) {
            logger.error("Erro ao buscar livro por ID: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody LivroDTO livroDTO) {
        try {
            logger.info("Tentando cadastrar um novo livro.");

            // Validações adicionais (se necessário)
            if (Objects.isNull(livroDTO) || livroDTO.getTituloLivro().isBlank()) {
                logger.warn("Título do livro é obrigatório.");
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "O título do livro é obrigatório."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            // Verifica duplicidade (baseado no título, por exemplo)
            if (livroRepository.existsByTituloLivro(livroDTO.getTituloLivro().trim()) ) {
                logger.warn("Livro com título '{}' já existe.", livroDTO.getTituloLivro());
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "Um livro com este título já existe."
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }

            // Converte DTO para entidade
            Livro livro = livroConverter.toEntity(livroDTO);

            // Salva o livro
            Livro livroCriado = livroRepository.save(livro);

            logger.info("Livro cadastrado com sucesso: ID = {}", livroCriado.getCodLivro());
            return ResponseEntity.status(HttpStatus.CREATED).body(livroCriado);
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violação de integridade de dados");
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            logger.error("Erro ao cadastrar livro: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/relatorio")
    public ResponseEntity<byte[]> gerarRelatorio() {
        try {
            // Caminho do relatório
            String caminhoRelatorio = "reports/livros.jrxml";
            InputStream relatorioStream = getClass().getClassLoader().getResourceAsStream(caminhoRelatorio);

            if (relatorioStream == null) {
                throw new JRException("Arquivo do relatório não encontrado: " + caminhoRelatorio);
            }

            // Compila o relatório
            JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);

            // Obter conexão do Hibernate
            Session session = entityManager.unwrap(Session.class);
            final Connection[] connectionHolder = new Connection[1];

            session.doWork(connection -> connectionHolder[0] = connection);

            Connection connection = connectionHolder[0];

            // Preenche o relatório
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), connection);

            // Exporta para PDF
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

            // Retorna o PDF como resposta
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-livros.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(outputStream.toByteArray());

        } catch (Exception e) {
            logger.error("Erro ao gerar relatório: " + e.getLocalizedMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<Livro> atualizar(@Valid @RequestBody Livro livro) {
        try {
            if (livro.getCodLivro() != null) {
                logger.info("Atualizando livro com ID: " + livro.getCodLivro());
                return ResponseEntity.ok(livroRepository.save(livro));
            }
            return ResponseEntity.unprocessableEntity().build();
        } catch (DataIntegrityViolationException ex) {
            logger.error("Violação de integridade de dados");
            return ResponseEntity.internalServerError().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar livro: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            logger.info("Deletando livro com ID: " + id);
            if (livroRepository.existsById(id)) {
                livroRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar livro: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
