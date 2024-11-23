CREATE SEQUENCE livro_seq
    START WITH 31
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE autor_seq
    START WITH 16
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE assunto_seq
    START WITH 11
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Configura as tabelas para usar as sequences nos campos ID
ALTER TABLE livro
    ALTER COLUMN cod_livro SET DEFAULT nextval('livro_seq');

ALTER TABLE autor
    ALTER COLUMN cod_autor SET DEFAULT nextval('autor_seq');

ALTER TABLE assunto
    ALTER COLUMN cod_assunto SET DEFAULT nextval('assunto_seq');