# CT1 — Cadastrar usuário

## 1. Objetivo

Definir o contrato HTTP utilizado para cadastrar um novo usuário no sistema QuemResolve.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: POST.
- Endpoint: `/api/usuarios`.
- Tipo: HTTP/JSON.

## 4. Entrada

O corpo da requisição deve conter:

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| nome | String | Sim | Nome do usuário |
| email | String | Sim | Endereço de e-mail |
| senha | String | Sim | Senha informada no cadastro |
| tipo | String | Sim | Tipo de usuário: `cliente` ou `prestador` |

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| usuarioId | Identificador | Identificador do usuário cadastrado |
| nome | String | Nome do usuário |
| email | String | E-mail cadastrado |
| tipo | String | Tipo de usuário |
| criadoEm | Data/hora | Data e hora do cadastro |

## 6. Regras de negócio

- Validar os campos obrigatórios.
- Verificar se o e-mail já está cadastrado.
- Aceitar os tipos de usuário previstos: `cliente` e `prestador`.
- Armazenar a senha como hash, sem persistir a senha em texto puro.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 409 | EMAIL_JA_CADASTRADO | O e-mail já está cadastrado |
| 400 | DADOS_INVALIDOS | Os dados enviados são inválidos |
| 500 | ERRO_INTERNO | Ocorreu um erro interno |

## 8. Fluxo relacionado

- E1 — Cadastro de usuário.
