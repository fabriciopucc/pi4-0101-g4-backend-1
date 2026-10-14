# CT5 — Pesquisar prestadores

## 1. Objetivo

Definir o contrato HTTP utilizado para pesquisar prestadores por categoria e localização.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: GET.
- Endpoint: `/api/prestadores`.
- Tipo: HTTP/JSON.

## 4. Entrada

Parâmetros de consulta:

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| categoria | String | Sim | Categoria de serviço desejada |
| cidade | String | Não | Cidade para a pesquisa |
| uf | String | Não | Unidade federativa |
| latitude | Decimal | Não | Latitude de referência |
| longitude | Decimal | Não | Longitude de referência |

A pesquisa deve utilizar a modalidade de localização prevista na especificação: cidade/UF ou coordenadas geográficas, conforme os parâmetros informados.

## 5. Saída

A resposta contém uma lista de objetos `ResumoPrestador` e o total de resultados.

Cada objeto pode conter:

| Campo | Tipo | Descrição |
|---|---|---|
| prestadorId | Identificador | Identificador do prestador |
| nomeProfissional | String | Nome profissional |
| categoria | String | Categoria correspondente |
| regiaoAtendimento | Lista de textos | Regiões atendidas |
| avaliacaoMedia | Decimal ou nulo | Média das avaliações, quando disponível |
| valorReferencia | Decimal | Preço de referência |

Além da lista, a resposta contém o campo `total`.

## 6. Regras de negócio

- A categoria é obrigatória.
- Os parâmetros de localização devem ser validados conforme a modalidade de pesquisa.
- Os resultados devem corresponder aos critérios informados.
- A média de avaliações pode não estar disponível.
- Uma pesquisa sem resultados deve retornar HTTP 200 com uma lista vazia.

## 7. Erros e exceções

- Categoria ausente.
- Localização ausente conforme a modalidade de pesquisa.
- Coordenadas inconsistentes.
- Falha interna ao processar a pesquisa.

## 8. Fluxos relacionados

- E3 — Pesquisa de prestadores.
- E4 — Exibição da lista de prestadores.
- E10 — Comunicação em tempo real, como alternativa de nova pesquisa após falha na conexão.
