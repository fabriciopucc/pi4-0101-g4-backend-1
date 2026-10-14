# CT14 — Autenticar usuário

## 1. Objetivo

Definir o contrato HTTP utilizado para autenticar um usuário no sistema QuemResolve.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: POST.
- Endpoint: `/api/usuarios/autenticar`.
- Tipo: HTTP/JSON.

## 4. Entrada

O corpo da requisição deve conter:

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| email | String | Sim | E-mail cadastrado |
| senha | String | Sim | Senha informada pelo usuário |

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| usuarioId | Identificador | Identificador do usuário autenticado |
| tipo | String | Tipo de usuário |
| tokenSessao | String | Token da sessão |
| expiraEm | Data/hora | Data e hora de expiração da sessão |

## 6. Regras de negócio

- O e-mail e a senha são obrigatórios.
- O backend deve validar as credenciais informadas.
- Quando a autenticação for bem-sucedida, o backend retorna os dados da sessão.
- As informações da sessão são utilizadas pelas funcionalidades que exigem autenticação.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 401 | CREDENCIAIS_INVALIDAS | As credenciais informadas são inválidas |
| 400 | DADOS_INVALIDOS | Os dados enviados são inválidos |
| 503 | SERVICO_INDISPONIVEL | O serviço está indisponível |

## 8. Fluxos relacionados

- E2 — Cadastro de prestador.
- E6 — Avaliação de prestador.
- E8 — Atualização do perfil do prestador.
