# CT10 — Atualizar perfil de prestador

## 1. Objetivo

Definir o contrato HTTP utilizado para atualizar os dados de um perfil profissional existente.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: PATCH.
- Endpoint: `/api/prestadores/{prestadorId}`.
- Tipo: HTTP/JSON.

## 4. Entrada

A requisição deve identificar o perfil na URL e conter os dados de autenticação e os campos que serão alterados.

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| tokenSessao | String | Sim | Token da sessão |
| prestadorId | Identificador | Sim | Identificador do perfil na URL |
| nomeProfissional | String | Não | Nome profissional atualizado |
| descricao | String | Não | Descrição atualizada |
| categorias | Lista de textos | Não | Categorias atualizadas |
| servicos | Lista de textos | Não | Serviços atualizados |
| regiaoAtendimento | Lista de textos | Não | Regiões atendidas atualizadas |
| valorReferencia | Decimal | Não | Preço de referência atualizado |
| tipoContato | String | Não | Tipo de contato atualizado |
| contatoValor | String | Não | Contato atualizado |
| latitude | Decimal | Não | Latitude atualizada |
| longitude | Decimal | Não | Longitude atualizada |
| raioKm | Decimal | Não | Raio de atendimento atualizado |

Os campos de perfil acima são exemplos das propriedades previstas para atualização; os campos efetivamente aceitos devem seguir a especificação e a implementação acordada.

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| prestadorId | Identificador | Identificador do perfil atualizado |
| atualizadoEm | Data/hora | Data e hora da atualização |
| atualizado | Booleano | Indica se a atualização foi realizada |

## 6. Regras de negócio

- O usuário deve estar autenticado.
- O backend deve verificar se o usuário possui autorização para alterar o perfil solicitado.
- O perfil deve existir.
- Os campos enviados devem ser validados.
- Os dados atualizados devem ser persistidos conforme CT4.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 403 | ACESSO_NEGADO | O usuário não tem permissão para atualizar o perfil |
| 404 | PRESTADOR_NAO_ENCONTRADO | O perfil não foi encontrado |
| 400 | DADOS_INVALIDOS | Os dados enviados são inválidos |
| 401 | NAO_AUTENTICADO | O usuário não está autenticado |

## 8. Fluxos relacionados

- E8 — Atualização do perfil do prestador.
- E10 — Comunicação em tempo real.
