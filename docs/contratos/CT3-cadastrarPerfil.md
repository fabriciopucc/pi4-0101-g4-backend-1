# CT3 — Cadastrar perfil de prestador

## 1. Objetivo

Definir o contrato HTTP utilizado para cadastrar o perfil profissional de um prestador.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: POST.
- Endpoint: `/api/prestadores`.
- Tipo: HTTP/JSON.

## 4. Entrada

O corpo da requisição deve conter os seguintes campos:

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| tokenSessao | String | Sim | Token da sessão do usuário |
| usuarioId | Identificador | Sim | Identificador do usuário |
| nomeProfissional | String | Sim | Nome profissional |
| descricao | String | Não | Descrição do prestador |
| categorias | Lista de textos | Sim | Categorias de serviços |
| servicos | Lista de textos | Sim | Serviços oferecidos |
| regiaoAtendimento | Lista de textos | Sim | Regiões atendidas |
| valorReferencia | Decimal | Sim | Preço de referência |
| tipoContato | Enum | Sim | `telefone`, `whatsapp` ou `link` |
| contatoValor | String | Sim | Valor ou endereço do contato |
| latitude | Decimal | Não | Latitude do prestador |
| longitude | Decimal | Não | Longitude do prestador |
| raioKm | Decimal | Não | Raio de atendimento em quilômetros |

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| prestadorId | Identificador | Identificador do perfil |
| usuarioId | Identificador | Identificador do usuário associado |
| atualizadoEm | Data/hora | Data e hora da atualização |

## 6. Regras de negócio

- O usuário deve estar autenticado.
- O usuário deve possuir o tipo `prestador`.
- Um usuário não pode cadastrar mais de um perfil, conforme a restrição definida na especificação.
- Os campos obrigatórios devem ser validados.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 403 | TIPO_USUARIO_INVALIDO | O usuário não possui o tipo permitido |
| 409 | PERFIL_JA_EXISTE | O usuário já possui um perfil |
| 400 | DADOS_INVALIDOS | Os dados enviados são inválidos |
| 401 | NAO_AUTENTICADO | O usuário não está autenticado |

## 8. Fluxo relacionado

- E2 — Cadastro de prestador.
