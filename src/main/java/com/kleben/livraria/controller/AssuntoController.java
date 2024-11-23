package com.kleben.livraria.controller;

import com.kleben.livraria.dto.AssuntoDTO;
import com.kleben.livraria.converter.AssuntoConverter;
import com.kleben.livraria.exception.ErrorResponse;
import com.kleben.livraria.model.Assunto;
import com.kleben.livraria.service.AssuntoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/assunto")
public class AssuntoController {

    private final AssuntoService assuntoService;
    private final AssuntoConverter assuntoConverter;

    public AssuntoController(AssuntoService assuntoService, AssuntoConverter assuntoConverter) {
        this.assuntoService = assuntoService;
        this.assuntoConverter = assuntoConverter;
    }

    private static final Logger logger = LoggerFactory.getLogger(AssuntoController.class);

    @GetMapping
    public ResponseEntity<Page<Assunto>> listar(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "10", required = false) @Max(100) Integer size) {
        try {
            logger.info("Listando assuntos");
            return ResponseEntity.ok(assuntoService.findAll(PageRequest.of(page, size)));
        } catch (Exception e) {
            logger.error("Erro ao listar assuntos: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Integer id) {
        try {
            logger.info("Buscando assunto com ID: {}", id);

            Optional<Assunto> assuntoOptional = assuntoService.buscarPorId(id);

            if (assuntoOptional.isPresent()) {
                return ResponseEntity.ok(assuntoOptional.get());
            } else {
                logger.warn("Assunto com ID {} não encontrado", id);
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Assunto não encontrado"
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar assunto com ID {}: {}", id, e.getLocalizedMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping
    public ResponseEntity<?> cadastrar(@Valid @RequestBody AssuntoDTO assuntoDTO) {
        try {
            if (Objects.isNull(assuntoDTO) || assuntoDTO.getDescricaoAssunto().isBlank()) {
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "A descrição do assunto é obrigatória."
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }

            if (assuntoService.findByDescricao(assuntoDTO.getDescricaoAssunto()) == null) {
                Assunto assunto = assuntoConverter.toEntity(assuntoDTO);
                Assunto assuntoCriado = assuntoService.salvar(assunto);
                return ResponseEntity.ok(assuntoCriado);
            } else {
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "A descrição do assunto já existe."
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            }
        } catch (Exception e) {
            logger.error("Erro ao cadastrar assunto: " + e.getLocalizedMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping
    public ResponseEntity<Assunto> atualizar(@Valid @RequestBody AssuntoDTO assuntoDTO) {
        try {
            if (assuntoDTO.getCodAssunto() != null) {
                return assuntoService.buscarPorId(assuntoDTO.getCodAssunto())
                        .map(assunto -> {
                            assunto.setDescricaoAssunto(assuntoDTO.getDescricaoAssunto());
                            return ResponseEntity.ok(assuntoService.salvar(assunto));
                        })
                        .orElse(ResponseEntity.notFound().build());
            }
            return ResponseEntity.unprocessableEntity().build();
        } catch (Exception e) {
            logger.error("Erro ao atualizar assunto: " + e.getLocalizedMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        try {
            logger.info("Tentando deletar o assunto com ID: {}", id);

            if (!assuntoService.existePorId(id)) {
                logger.warn("Assunto com ID {} não encontrado", id);
                return ResponseEntity.notFound().build();
            }

            if (assuntoService.estaAssociadoALivro(id)) {
                logger.warn("Assunto com ID {} está associado a um ou mais livros e não pode ser deletado", id);
                ErrorResponse error = new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "O Assunto com está associado a um ou mais livros e não pode ser deletado"
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
//                return ResponseEntity.status(HttpStatus.CONFLICT).build(); // HTTP 409
            }

            assuntoService.deletarPorId(id);
            logger.info("Assunto com ID {} deletado com sucesso", id);
            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            logger.error("Erro ao deletar o assunto com ID {}: {}", id, e.getLocalizedMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }


}
