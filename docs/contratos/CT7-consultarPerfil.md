# CT7 — Consultar perfil de prestador

## 1. Objetivo

Definir o contrato HTTP utilizado para consultar as informações públicas de um prestador.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: GET.
- Endpoint: `/api/prestadores/{prestadorId}`.
- Tipo: HTTP/JSON.

## 4. Entrada

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| prestadorId | Identificador | Sim | Identificador do prestador na URL |

## 5. Saída

A resposta contém o objeto `perfil`, representando um `PerfilPublico`.

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| prestadorId | Identificador | Sim | Identificador do perfil |
| nomeProfissional | String | Sim | Nome profissional |
| descricao | String | Não | Descrição do prestador |
| categorias | Lista de textos | Sim | Categorias de serviços |
| servicos | Lista de textos | Sim | Serviços oferecidos |
| regiaoAtendimento | Lista de textos | Sim | Regiões atendidas |
| valorReferencia | Decimal | Sim | Preço de referência |
| avaliacaoMedia | Decimal ou nulo | Não | Média das avaliações |
| avaliacoes | Lista | Sim | Avaliações públicas disponíveis |

Cada item de `avaliacoes` é um objeto `AvaliacaoPublica` com:

- `avaliacaoId`
- `nota`
- `comentario`, quando disponível
- `criadoEm`

## 6. Regras de negócio

- O identificador deve ser válido.
- O perfil deve existir para que a consulta seja concluída.
- A resposta deve conter as informações públicas previstas na especificação.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 404 | PRESTADOR_NAO_ENCONTRADO | O prestador não foi encontrado |
| 400 | PRESTADOR_ID_INVALIDO | O identificador informado é inválido |

## 8. Fluxo relacionado

- E5 — Consulta ao perfil do prestador.
