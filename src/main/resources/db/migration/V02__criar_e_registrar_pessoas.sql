CREATE TABLE pessoa (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
    logradouro VARCHAR(50) NULL,
    numero VARCHAR(5) NULL,
    complemento VARCHAR(20) NULL,
    bairro VARCHAR(30) NULL,
    cep VARCHAR(8) NULL,
    cidade VARCHAR(50) NULL,
    estado VARCHAR(2) NULL,
	ativo TINYINT(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Flávio Roberto', 'SQNW 309 Bloco B', '212', 'Apto', 'Setor Noroeste', '70687110', 'Brasília', 'DF', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Érica Jordana', 'SQNW 309 Bloco B', '212', 'Apto', 'Setor Noroeste', '70687110', 'Brasília', 'DF', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Auriceia Cruz Silva', 'Quadra 16 Lote 17', 'S/N', '', 'Jardim Tropical', '72000000', 'Aparecidade de Goiânia', 'GO', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) VALUES ('Maria José Bento Viana', 'Rua Josué Pereira', '18', '', 'Afogados', '83460129', 'Recife', 'PE', true);