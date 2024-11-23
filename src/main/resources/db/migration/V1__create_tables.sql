CREATE TABLE livro (
                       cod_livro SERIAL PRIMARY KEY,
                       titulo_livro VARCHAR(40) NOT NULL,
                       editora_livro VARCHAR(40) NOT NULL,
                       edicao_livro INTEGER NOT NULL,
                       ano_publicacao_livro VARCHAR(4) NOT NULL,
                       preco NUMERIC(10, 2) NOT NULL DEFAULT 0.00
);

CREATE TABLE autor (
                       cod_autor INTEGER PRIMARY KEY,
                       nome_autor VARCHAR(40) NOT NULL
);

CREATE TABLE livro_autor (
                             cod_livro INTEGER,
                             cod_autor INTEGER,
                             PRIMARY KEY (cod_livro, cod_autor),
                             FOREIGN KEY (cod_livro) REFERENCES livro (cod_livro),
                             FOREIGN KEY (cod_autor) REFERENCES autor (cod_autor)
);

CREATE TABLE assunto (
                         cod_assunto INTEGER PRIMARY KEY,
                         descricao_assunto VARCHAR(20) NOT NULL
);

CREATE TABLE livro_assunto (
                               cod_livro INTEGER,
                               cod_assunto INTEGER,
                               PRIMARY KEY (cod_livro, cod_assunto),
                               FOREIGN KEY (cod_livro) REFERENCES livro (cod_livro),
                               FOREIGN KEY (cod_assunto) REFERENCES assunto (cod_assunto)
);
