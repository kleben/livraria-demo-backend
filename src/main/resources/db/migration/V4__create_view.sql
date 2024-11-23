CREATE OR REPLACE VIEW livro_detalhes AS
SELECT
    livro.cod_livro,
    livro.titulo_livro,
    livro.editora_livro,
    livro.edicao_livro,
    livro.ano_publicacao_livro,
    STRING_AGG(DISTINCT autor.nome_autor, ', ') AS nome_autores,
    STRING_AGG(DISTINCT assunto.descricao_assunto, ', ') AS descricao_assuntos
FROM
    livro
        LEFT JOIN livro_autor ON livro.cod_livro = livro_autor.cod_livro
        LEFT JOIN autor ON livro_autor.cod_autor = autor.cod_autor
        LEFT JOIN livro_assunto ON livro.cod_livro = livro_assunto.cod_livro
        LEFT JOIN assunto ON livro_assunto.cod_assunto = assunto.cod_assunto
GROUP BY
    livro.cod_livro,
    livro.titulo_livro,
    livro.editora_livro,
    livro.edicao_livro,
    livro.ano_publicacao_livro
ORDER BY livro.titulo_livro;
