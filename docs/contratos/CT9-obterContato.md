# CT9 — Obter contato de prestador

## 1. Objetivo

Definir o contrato HTTP utilizado para obter as informações de contato de um prestador.

## 2. Responsáveis

- Emissor: Frontend.
- Receptor: Backend.

## 3. Operação

- Método HTTP: GET.
- Endpoint: `/api/prestadores/{prestadorId}/contato`.
- Tipo: HTTP/JSON.

## 4. Entrada

| Parâmetro | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| prestadorId | Identificador | Sim | Identificador do prestador na URL |

## 5. Saída

Em caso de sucesso, retornar:

| Campo | Tipo | Descrição |
|---|---|---|
| tipoContato | String | Tipo de contato cadastrado |
| contatoValor | String | Valor ou endereço do contato |
| urlDestino | String | URL de destino, quando disponível |

## 6. Regras de negócio

- O prestador deve existir.
- O backend deve consultar as informações de contato do perfil.
- O tipo de contato e o destino devem corresponder aos dados cadastrados.
- O destino será utilizado pelo frontend para abrir o canal externo.

## 7. Erros e exceções

| Código HTTP | Código de erro | Descrição |
|---|---|---|
| 404 | PRESTADOR_NAO_ENCONTRADO | O prestador não foi encontrado |
| 422 | CONTATO_INDISPONIVEL | Não existe um contato disponível |

## 8. Fluxo relacionado

- E7 — Contato com prestador.
