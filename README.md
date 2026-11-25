# Oficina Mecânica: Design Patterns (Composite + Strategy)

> ⚠️ **Equipe: leiam o [`ENCAMINHAMENTO.md`](ENCAMINHAMENTO.md) antes de começar.**
> Ele define as assinaturas exatas de cada classe, a ordem dos commits e o que cada integrante deve entregar.
> Sempre rode `git pull` antes de começar e antes de dar `git push`.
>
> *(Este aviso deve ser removido pela Lara na versão final do README.)*

## Status do projeto

| Parte | Responsável | Situação |
|---|---|---|
| Estrutura Maven, `.gitignore`, `OrdemServico` | Pedro Ernesto | ✅ Concluído |
| `Veiculo` + Composite (`ItemOrdemServico`, `ItemSimples`, `PacoteServico`) | Carlos Eduardo | ✅ Concluído |
| Strategy (`EstrategiaOrcamento`, `OrcamentoPadrao`, `OrcamentoComDesconto`) | Mateus Leal | ✅ Concluído |
| `Main` + README final | Lara Reck | ⏳ Pendente |

## Descrição do sistema

*(a completar: Lara)*

## Tecnologias e pré-requisitos

- Java 17+
- Maven 3.8+
- VS Code com o *Extension Pack for Java*

## Como executar

*(a completar: Lara)*

## Exemplo de saída

*(a completar: Lara)*

## Padrões de projeto utilizados

### Composite

*(a completar: Lara)*

### Strategy

*(a completar: Lara)*

## Declaração de uso de IA

O Copilot foi utilizado como apoio no desenvolvimento da parte de Carlos Eduardo.

O Claude Sonnet 5.5 (Claude Code) foi usado para gerar o padrão Strategy, seus testes, a configuração de testes no `pom.xml` e a validação de ciclos em `PacoteServico` (com teste).

## Contribuição de cada integrante

- **Pedro Ernesto:** configuração inicial do repositório, estrutura Maven (`pom.xml`, `.gitignore`),
  implementação de `OrdemServico` (Context do Strategy e Client do Composite), definição dos contratos
  entre as classes (`ENCAMINHAMENTO.md`) e revisão geral da arquitetura.
- **Carlos Eduardo:** implementou `Veiculo` e o padrão Composite (`ItemOrdemServico`, `ItemSimples` e `PacoteServico`), incluindo cálculo recursivo de valores e validação das entradas.
- **Mateus Leal:** implementação do padrão Strategy (`EstrategiaOrcamento`, `OrcamentoPadrao` e `OrcamentoComDesconto`) e seus testes; correção de ciclo em `PacoteServico`.
- **Lara Reck:** *(a completar)*
