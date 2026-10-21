-- =====================================================================
--  Magic Card Tracker - script do banco de dados (PostgreSQL)
--
--  Como usar:
--    1) Crie um banco vazio chamado MagicCardTracker
--         CREATE DATABASE "MagicCardTracker";
--    2) Conecte nele e rode este script inteiro.
--
--  A conexão do projeto (dao/IConst.java) espera:
--    jdbc:postgresql://localhost:5432/MagicCardTracker  |  usuário: postgres  |  senha: postgres
-- =====================================================================

-- ---------------------------------------------------------------------
--  Tabelas
-- ---------------------------------------------------------------------
CREATE TABLE Usuario (
    usuarioId   SERIAL PRIMARY KEY,
    nomeUsuario VARCHAR(100) NOT NULL UNIQUE,
    senha       VARCHAR(255) NOT NULL
);

CREATE TABLE Carta (
    cartaId     SERIAL PRIMARY KEY,
    nomeCarta   VARCHAR(150) NOT NULL UNIQUE,
    dano        INT,
    resistencia INT,
    imagem      BYTEA
);

CREATE TABLE Supertipo (
    supertipoId   SERIAL PRIMARY KEY,
    nomeSupertipo VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Tipo (
    tipoId   SERIAL PRIMARY KEY,
    nomeTipo VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Subtipo (
    subtipoId   SERIAL PRIMARY KEY,
    nomeSubtipo VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Cor (
    corId   SERIAL PRIMARY KEY,
    nomeCor VARCHAR(30) NOT NULL UNIQUE
);

-- Relações N:N entre a carta e seus atributos
CREATE TABLE TemSupertipo (
    supertipoId INT NOT NULL REFERENCES Supertipo(supertipoId),
    cartaId     INT NOT NULL REFERENCES Carta(cartaId) ON DELETE CASCADE,
    PRIMARY KEY (supertipoId, cartaId)
);

CREATE TABLE TemTipo (
    tipoId  INT NOT NULL REFERENCES Tipo(tipoId),
    cartaId INT NOT NULL REFERENCES Carta(cartaId) ON DELETE CASCADE,
    PRIMARY KEY (tipoId, cartaId)
);

CREATE TABLE TemSubtipo (
    subtipoId INT NOT NULL REFERENCES Subtipo(subtipoId),
    cartaId   INT NOT NULL REFERENCES Carta(cartaId) ON DELETE CASCADE,
    PRIMARY KEY (subtipoId, cartaId)
);

CREATE TABLE TemCor (
    corId   INT NOT NULL REFERENCES Cor(corId),
    cartaId INT NOT NULL REFERENCES Carta(cartaId) ON DELETE CASCADE,
    PRIMARY KEY (corId, cartaId)
);

-- Coleção do usuário: cada usuário tem no máximo uma unidade de cada carta
CREATE TABLE TemCarta (
    usuarioId INT NOT NULL REFERENCES Usuario(usuarioId) ON DELETE CASCADE,
    cartaId   INT NOT NULL REFERENCES Carta(cartaId) ON DELETE CASCADE,
    PRIMARY KEY (usuarioId, cartaId)
);

-- ---------------------------------------------------------------------
--  Dados iniciais (sem eles não é possível cadastrar cartas)
-- ---------------------------------------------------------------------
INSERT INTO Tipo (nomeTipo) VALUES
    ('Artefato'), ('Criatura'), ('Encantamento'), ('Feitiço'),
    ('Mágica Instantânea'), ('Planeswalker'), ('Terreno'), ('Batalha'), ('Tribal')
ON CONFLICT DO NOTHING;

INSERT INTO Supertipo (nomeSupertipo) VALUES
    ('Básico'), ('Lendário'), ('Neve'), ('Mundo')
ON CONFLICT DO NOTHING;

INSERT INTO Cor (nomeCor) VALUES
    ('Branco'), ('Azul'), ('Preto'), ('Vermelho'), ('Verde'), ('Incolor')
ON CONFLICT DO NOTHING;

INSERT INTO Subtipo (nomeSubtipo) VALUES
    ('Human'), ('Elf'), ('Goblin'), ('Dragon'), ('Angel'), ('Zombie'),
    ('Warrior'), ('Wizard'), ('Druid'), ('Rogue'), ('Frog'), ('Lizard'),
    ('Ooze'), ('Equipment'), ('Aura'), ('Saga'), ('Class'),
    ('Forest'), ('Island'), ('Plains'), ('Swamp'), ('Mountain')
ON CONFLICT DO NOTHING;
