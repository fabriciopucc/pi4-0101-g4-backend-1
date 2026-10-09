# ADR 0001: Usar WebSocket para comunicação em tempo real

**Status:** aceito

**Contexto:** O entregável E10 do QuemResolve prevê comunicação por WebSocket nas operações que exigem atualização em tempo real. O grupo definiu o backend-1 como responsável por esse canal e o backend-2 pelas demais funcionalidades. Chat interno e gestão de agenda e disponibilidade em tempo real estão fora do escopo.

**Decisão:** Implementar no backend-1 o canal WebSocket entre navegador e servidor para as operações que exigirem atualização em tempo real. Delimitar seu uso às funcionalidades previstas no escopo.

**Alternativas consideradas:**
- Consultas HTTP periódicas (polling): descartadas por gerar requisições mesmo sem alterações e limitar a atualização ao intervalo das consultas.
- Server-Sent Events (SSE): descartados por não atenderem à escolha explícita de WebSocket no entregável E10, embora permitam notificações do servidor ao navegador.

**Consequências:**
- Positivas: o servidor poderá enviar atualizações ao navegador sem recarga manual; esse comportamento poderá ser verificado em duas sessões conectadas durante uma operação que produza atualização.
- Negativas: será necessário controlar conexões abertas, validar acesso, implementar reconexão e sincronizar dados após interrupções; a separação entre backends também exigirá definir e manter o contrato de integração para encaminhar as atualizações.
