# CT8 — Registrar avaliação

## 1. Objetivo

Definir o contrato HTTP utilizado para registrar uma avaliação de um prestador por um cliente.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: POST.
- Endpoint: `/api/prestadores/{prestadorId}/avaliacoes`.
- Tipo: HTTP/JSON.

## 4. Entrada

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| tokenSessao | String | Sim | Token da sessão do usuário |
| prestadorId | Identificador | Sim | Identificador do prestador |
| clienteId | Identificador | Sim | Identificador do cliente |
| nota | Inteiro | Sim | Nota de 1 a 5 |
| comentario | String | Não | Comentário da avaliação |
| confirmouAtendimento | Booleano | Sim | Confirmação do atendimento |

O identificador `prestadorId` também é informado no caminho da URL.

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| avaliacaoId | Identificador | Identificador da avaliação criada |
| prestadorId | Identificador | Prestador avaliado |
| nota | Inteiro | Nota registrada |
| avaliacaoMedia | Decimal | Média atualizada das avaliações |
| criadoEm | Data/hora | Data e hora do registro |

## 6. Regras de negócio

- O usuário deve estar autenticado.
- A nota deve estar entre 1 e 5.
- O cliente deve confirmar que o atendimento ocorreu.
- O prestador deve existir.
- A avaliação deve ser persistida conforme CT6.
- A média das avaliações deve ser recalculada pelo backend.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 400 | NOTA_INVALIDA | A nota está fora do intervalo permitido |
| 400 | ATENDIMENTO_NAO_CONFIRMADO | O atendimento não foi confirmado |
| 401 | NAO_AUTENTICADO | O usuário não está autenticado |
| 404 | PRESTADOR_NAO_ENCONTRADO | O prestador não foi encontrado |

## 8. Fluxo relacionado

- E6 — Avaliação de prestador.
