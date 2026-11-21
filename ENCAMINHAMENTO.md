# Encaminhamento da equipe: contratos e ordem dos commits

> Documento de coordenação interna (Pedro Ernesto). Ele define **as assinaturas públicas** que cada
> integrante deve implementar para que as partes se encaixem sem conflito. Depois que todos
> commitarem, a Lara pode incorporar o que for útil ao `README.md` e este arquivo pode ser removido.

## O que já está no repositório (Pedro Ernesto)

| Arquivo | Conteúdo |
|---|---|
| `.gitignore` | Java, Maven, VS Code (`.vscode/`), Eclipse/JDT, IntelliJ, SO |
| `pom.xml` | `br.com.oficina:oficina-mecanica-patterns`, Java 17, UTF-8, `exec-maven-plugin` → `br.com.oficina.Main` |
| `src/main/java/br/com/oficina/model/OrdemServico.java` | Context do Strategy e Client do Composite |

**Atenção:** o projeto **só compila depois que Carlos e Mateus subirem suas classes**, e só roda
(`exec:java`) depois que a Lara subir o `Main.java`. Isso é esperado.

## Ordem dos commits

1. **Pedro**: `.gitignore`, `pom.xml`, `OrdemServico.java`, este arquivo ✅
2. **Carlos**: `git pull` → `model/Veiculo.java` + pasta `composite/`
3. **Mateus**: `git pull` → pasta `strategy/` (a partir daqui `mvn compile` deve passar)
4. **Lara**: `git pull` → `Main.java` + `README.md` (a partir daqui `mvn compile exec:java` roda)

Use sempre `git pull` antes de começar e antes de dar `git push`.

## O que o `OrdemServico` usa das classes de vocês

```java
// Construtores
new OrdemServico(int numero, Veiculo veiculo)                              // usa new OrcamentoPadrao()
new OrdemServico(int numero, Veiculo veiculo, EstrategiaOrcamento estrategia)

// Métodos públicos
void adicionarItem(ItemOrdemServico item)
boolean removerItem(ItemOrdemServico item)
List<ItemOrdemServico> getItens()          // somente leitura
double calcularSubtotal()                  // soma item.calcularValor() de todos os itens
void trocarEstrategia(EstrategiaOrcamento nova)
EstrategiaOrcamento getEstrategia()
double calcularTotal()                     // delega: estrategia.calcularTotal(this)
int getNumero()
Veiculo getVeiculo()
String toString()                          // usa veiculo.toString() e estrategia.getNomeEstrategia()
```

---

## Carlos Eduardo: `Veiculo` + Composite

### `br.com.oficina.model.Veiculo`

```java
public Veiculo(String placa, String modelo, int ano, String proprietario)
public String getPlaca()
public String getModelo()
public int getAno()
public String getProprietario()
@Override public String toString()   // ex.: "ABC-1D23 - Fiat Argo (2021) - Prop.: João Silva"
```

### `br.com.oficina.composite.ItemOrdemServico` (interface Component)

```java
public interface ItemOrdemServico {
    String getDescricao();          // nome legível do item/pacote
    double calcularValor();         // valor total deste item (pacote = soma recursiva)
    String exibir(int nivel);       // texto formatado; "nivel" = profundidade, para indentação
}
```

### `br.com.oficina.composite.ItemSimples` (Leaf)

```java
public enum Tipo { PECA, SERVICO }  // enum aninhado: ItemSimples.Tipo

public ItemSimples(String nome, Tipo tipo, double valorUnitario, int quantidade)
public String getNome()
public Tipo getTipo()
public double getValorUnitario()
public int getQuantidade()
// calcularValor() = valorUnitario * quantidade
```

### `br.com.oficina.composite.PacoteServico` (Composite)

```java
public PacoteServico(String nome)
public void adicionarItem(ItemOrdemServico item)     // aceita ItemSimples OU outro PacoteServico
public boolean removerItem(ItemOrdemServico item)
public List<ItemOrdemServico> getItens()             // somente leitura
// calcularValor() = soma recursiva de item.calcularValor()
// exibir(nivel)   = linha do pacote + exibir(nivel + 1) de cada filho
```

Sugestão: validar entradas (nome não vazio, valor ≥ 0, quantidade > 0) com `IllegalArgumentException`,
como o `OrdemServico` já faz.

---

## Mateus Leal: Strategy

### `br.com.oficina.strategy.EstrategiaOrcamento` (interface Strategy)

```java
public interface EstrategiaOrcamento {
    double calcularTotal(OrdemServico os);
    String getNomeEstrategia();
}
```

⚠️ **Regra importante:** dentro de `calcularTotal(os)` use **`os.calcularSubtotal()`** (ou `os.getItens()`).
**Nunca chame `os.calcularTotal()`**, porque o `OrdemServico.calcularTotal()` chama a estratégia, e isso
geraria recursão infinita (`StackOverflowError`).

### `br.com.oficina.strategy.OrcamentoPadrao`

```java
public OrcamentoPadrao()                     // construtor sem argumentos (o OrdemServico usa)
// calcularTotal(os) = os.calcularSubtotal()
// getNomeEstrategia() = "Orçamento Padrão"
```

### `br.com.oficina.strategy.OrcamentoComDesconto`

```java
public OrcamentoComDesconto(double percentualDesconto)   // em pontos percentuais: 10.0 = 10%
public double getPercentualDesconto()
// calcularTotal(os) = subtotal - subtotal * percentual / 100
// getNomeEstrategia() = "Orçamento com Desconto (10,0%)"
```

Sugestão: rejeitar percentual fora de `0..100` com `IllegalArgumentException`.

---

## Lara Reck: `Main` + `README.md`

### `br.com.oficina.Main`

Precisa ter `public static void main(String[] args)` (o `pom.xml` já aponta para essa classe).
Fluxo esperado pelo enunciado:

1. criar um `Veiculo`;
2. abrir uma `OrdemServico`;
3. adicionar itens simples (`ItemSimples`, PEÇA e SERVIÇO);
4. criar um `PacoteServico` de revisão (de preferência com um subpacote dentro, para mostrar a recursão) e adicioná-lo;
5. exibir o detalhamento (`item.exibir(0)` para cada `os.getItens()`), o subtotal e o total com `OrcamentoPadrao`;
6. `os.trocarEstrategia(new OrcamentoComDesconto(...))` e exibir o novo total.

Dica: para formatar como moeda, use `String.format(new java.util.Locale("pt", "BR"), "R$ %,.2f", valor)`.

Dica de acentuação: se no terminal do Windows aparecer `Or�amento` em vez de `Orçamento`, o problema é do
console, não do código. Antes de rodar no PowerShell, execute:
`chcp 65001; $env:MAVEN_OPTS="-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8"`. Vale citar no README.

### `README.md`: itens obrigatórios

- Descrição do sistema
- Tecnologias e pré-requisitos (Java 17+, Maven 3.8+, VS Code)
- Execução: `mvn compile exec:java` e pelo botão **Run** do VS Code (Extension Pack for Java)
- Exemplo de saída do console (copiar a saída real depois de rodar)
- Explicação do Composite e do Strategy: classes envolvidas e problema resolvido
- **Declaração de uso de IA:** o modelo **Claude Opus 5.5** (via Claude Code) foi usado como apoio na
  geração da estrutura inicial do projeto Maven, classes base e documentação; tudo revisado e validado pela equipe.
- Contribuição de cada integrante (Pedro, Carlos, Mateus, Lara), conforme a divisão combinada.

---

## Checklist final (quem fizer o último commit)

- [ ] `mvn compile exec:java` roda sem erros
- [ ] Nenhum pattern além de Composite e Strategy
- [ ] Nenhum banco de dados (tudo em memória)
- [ ] README com declaração de IA e contribuições
