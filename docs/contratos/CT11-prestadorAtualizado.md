# CT11 — Evento de prestador atualizado

## 1. Objetivo

Definir o evento WebSocket utilizado pelo backend para comunicar atualizações relevantes nos perfis dos prestadores.

## 2. Responsáveis

- Emissor: Backend.
- Receptor: Frontend.

## 3. Operação

- Tipo: WebSocket.
- Canal: `/ws/prestadores`.
- Nome do evento: `prestadorAtualizado`.

## 4. Dados do evento

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| prestadorId | Identificador | Sim | Identificador do prestador |
| nomeProfissional | String | Sim | Nome profissional |
| categorias | Lista de textos | Sim | Categorias do prestador |
| regiaoAtendimento | Lista de textos | Sim | Regiões atendidas |
| avaliacaoMedia | Decimal ou nulo | Não | Média das avaliações, quando disponível |
| atualizadoEm | Data/hora | Sim | Data e hora da atualização |

## 5. Regras de negócio

- O backend emite o evento quando ocorrer uma atualização relevante prevista na especificação.
- O evento deve conter os dados definidos neste contrato.
- O frontend utiliza o evento para atualizar as informações apresentadas aos usuários inscritos.
- A emissão do evento não substitui a persistência dos dados.

## 6. Resposta

Este contrato define um evento de notificação. Não há uma resposta específica prevista para o evento.

## 7. Erros e exceções

- Falha na conexão WebSocket.
- Falha no envio do evento.
- Frontend desconectado ou indisponível para receber a notificação.

A estratégia de reconexão e recuperação deve seguir a implementação definida pelo grupo.

## 8. Fluxos relacionados

- E6 — Avaliação de prestador.
- E8 — Atualização do perfil do prestador.
- E10 — Comunicação em tempo real.
