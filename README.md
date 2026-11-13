# Gestão das Torres de Resfriamento — backend

Spring Boot 3 (Java 17) + PostgreSQL + Flyway. O frontend (idêntico ao protótipo aprovado) é servido pelo próprio Spring Boot em `src/main/resources/static/index.html`.

## Executar
1. `docker compose up -d`   (PostgreSQL em localhost:5432; ou aponte `DB_URL`, `DB_USER`, `DB_PASSWORD` para o seu banco)
2. `mvn spring-boot:run`    (requer JDK 17 e Maven 3.9+)
3. Abra http://localhost:8080

O schema e as 12 torres / 9 componentes iniciais são criados automaticamente (`db/migration/V1__schema.sql`).

## API
| Método | Rota | Função |
|---|---|---|
| GET | `/api/estado` | Condição atual de cada conjunto/área + todas as sinalizações com rastreio |
| POST | `/api/sinalizacoes` | `{torre, alvo:"G"\|"1".."6", condicao:"D"\|"P"\|"I", itens:[{componente, justificativa, fotos}]}` |
| POST | `/api/sinalizacoes/{id}/atendimentos` | `{situacao:"A"\|"E"\|"R", responsavel, descricao}` (descrição obrigatória ao resolver) |

Regras: sinalizações nunca são apagadas nem sobrescritas; a resolução grava um `atendimento` e preserva o registro original; erros voltam como `{"mensagem": "..."}`.

## Ainda não incluído
Login e perfis (Operador / Manutenção / Administrador), cadastros administrativos, upload real de fotos (hoje só a quantidade é salva) e testes automatizados.
