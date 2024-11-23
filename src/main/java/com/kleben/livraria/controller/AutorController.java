package com.kleben.livraria.controller;

import com.kleben.livraria.converter.AssuntoConverter;
import com.kleben.livraria.converter.AutorConverter;
import com.kleben.livraria.dto.AutorDTO;
import com.kleben.livraria.exception.ErrorResponse;
import com.kleben.livraria.model.Autor;
import com.kleben.livraria.repository.AutorRepository;
import com.kleben.livraria.service.AssuntoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorRepository autorRepository;
    private final AutorConverter autorConverter;
    private static final Logger logger = LoggerFactory.getLogger(AutorController.class);

    public AutorController(AutorConverter assuntoConverter) {
        this.autorConverter = assuntoConverter;
    }

    @GetMapping
    public ResponseEntity<Page<Autor>> listar(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size) {
        try {
            logger.info("Listando autores");
            return ResponseEntity.ok(autorRepository.findAll(PageRequest.of(page, size)));
        } catch (Exception e) {
            logger.error("Erro ao listar autores: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<Autor>> buscarPorId(@PathVariable Integer id) {
        try {
            logger.info("Buscando autor com ID: " + id);
            return ResponseEntity.ok(autorRepository.findById(id));
        } catch (Exception e) {
            logger.error("Erro ao buscar autor: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody AutorDTO autorDTO) {
        try {
            if (Objects.isNull(autorDTO) || autorDTO.getNomeAutor().isBlank()) {
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "O nome do autor é obrigatório."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (autorRepository.findByNomeAutor(autorDTO.getNomeAutor()) == null) {
                Autor autor = autorConverter.toEntity(autorDTO);
                Autor autorCriado = autorRepository.save(autor);
                return ResponseEntity.ok(autorCriado);
            } else {
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "O nome do autor já existe."
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
        } catch (Exception e) {
            logger.error("Erro ao cadastrar autor: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping
    public ResponseEntity<Autor> atualizar(@Valid @RequestBody AutorDTO autorDTO) {
        try {
            if (autorDTO.getCodAutor() != null) {
                Optional<Autor> autorOptional = autorRepository.findById(autorDTO.getCodAutor());
                if (autorOptional.isPresent()) {
                    Autor autor = autorOptional.get();
                    autor.setNomeAutor(autorDTO.getNomeAutor());
                    return ResponseEntity.ok(autorRepository.save(autor));
                }
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar autor: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        try {
            logger.info("Deletando autor com ID: " + id);
            if (autorRepository.existsById(id)) {
                autorRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Erro ao deletar autor: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
