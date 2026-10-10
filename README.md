# TechFix: Sistema de Ordens de Serviço

API REST para controlar os consertos de uma assistência técnica: cadastro de clientes e equipamentos, abertura de OS, orçamento, andamento do conserto e entrega.

## Tecnologias

- Java 21 e Spring Boot 3.5
- PostgreSQL 16 e Flyway
- JUnit 5 e Mockito
- Docker
- Swagger

## Destaques

- **Padrão State:** cada status da OS sabe para quais outros pode ir (ex.: uma OS entregue não volta para recebida).
- **Padrão Strategy:** o orçamento é calculado por valor fixo ou por hora trabalhada, mais o valor das peças.

## Como rodar

```bash
docker compose up -d
mvn spring-boot:run
```

Documentação da API: http://localhost:8080/swagger-ui.html

Testes:

```bash
mvn test
```

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| POST | `/api/clientes` | Cadastra cliente |
| GET | `/api/clientes/{id}` | Busca cliente |
| POST | `/api/equipamentos` | Cadastra equipamento |
| POST | `/api/ordens-servico` | Abre OS |
| GET | `/api/ordens-servico/{numero}` | Consulta OS |
| PUT | `/api/ordens-servico/{numero}/orcamento` | Registra orçamento |
| PATCH | `/api/ordens-servico/{numero}/status` | Altera status |

## Autor

Rian Arruda Costa: [GitHub](https://github.com/Rian-dev-cod)