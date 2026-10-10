-- V1: estrutura inicial do sistema de Ordens de Serviço

CREATE TABLE cliente (
    id         BIGSERIAL    PRIMARY KEY,
    nome       VARCHAR(120) NOT NULL,
    telefone   VARCHAR(20)  NOT NULL,
    email      VARCHAR(120),
    criado_em  TIMESTAMP    NOT NULL
);

CREATE TABLE equipamento (
    id            BIGSERIAL   PRIMARY KEY,
    tipo          VARCHAR(20) NOT NULL,
    marca         VARCHAR(60) NOT NULL,
    modelo        VARCHAR(60) NOT NULL,
    numero_serie  VARCHAR(60),
    cliente_id    BIGINT      NOT NULL REFERENCES cliente (id)
);

CREATE INDEX idx_equipamento_cliente ON equipamento (cliente_id);

CREATE TABLE ordem_servico (
    id                BIGSERIAL     PRIMARY KEY,
    equipamento_id    BIGINT        NOT NULL REFERENCES equipamento (id),
    defeito_relatado  VARCHAR(500)  NOT NULL,
    diagnostico       VARCHAR(1000),
    valor_orcamento   NUMERIC(10, 2),
    status            VARCHAR(30)   NOT NULL,
    data_entrada      TIMESTAMP     NOT NULL,
    previsao_entrega  DATE,
    data_entrega      TIMESTAMP
);

CREATE INDEX idx_os_status ON ordem_servico (status);
CREATE INDEX idx_os_previsao ON ordem_servico (previsao_entrega);
