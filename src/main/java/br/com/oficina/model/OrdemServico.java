package br.com.oficina.model;

import br.com.oficina.composite.ItemOrdemServico;
import br.com.oficina.strategy.EstrategiaOrcamento;
import br.com.oficina.strategy.OrcamentoPadrao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Ordem de Serviço (OS) da oficina mecânica.
 *
 * <p>Papel nos padrões de projeto:</p>
 * <ul>
 *   <li><b>Composite</b> — é o <i>Client</i>: guarda uma lista de {@link ItemOrdemServico}
 *       e trata itens simples e pacotes de forma uniforme, sem precisar saber qual é qual.</li>
 *   <li><b>Strategy</b> — é o <i>Context</i>: mantém uma referência a uma
 *       {@link EstrategiaOrcamento} e delega a ela o cálculo do total final.
 *       A estratégia pode ser trocada em tempo de execução por {@link #trocarEstrategia}.</li>
 * </ul>
 *
 * <p>Pontos de extensão (Parte 2): novos tipos de item e novas regras de orçamento
 * funcionam aqui sem nenhuma alteração nesta classe, bastando implementar
 * {@link ItemOrdemServico} ou {@link EstrategiaOrcamento}.</p>
 */
public class OrdemServico {

    private final int numero;
    private final Veiculo veiculo;
    private final List<ItemOrdemServico> itens = new ArrayList<>();
    private EstrategiaOrcamento estrategia;

    /**
     * Abre uma OS usando a estratégia {@link OrcamentoPadrao} (soma integral dos itens).
     *
     * @param numero  número identificador da OS (maior que zero)
     * @param veiculo veículo atendido
     */
    public OrdemServico(int numero, Veiculo veiculo) {
        this(numero, veiculo, new OrcamentoPadrao());
    }

    /**
     * Abre uma OS já com uma estratégia de orçamento definida.
     *
     * @param numero     número identificador da OS (maior que zero)
     * @param veiculo    veículo atendido
     * @param estrategia regra de cálculo do orçamento
     */
    public OrdemServico(int numero, Veiculo veiculo, EstrategiaOrcamento estrategia) {
        if (numero <= 0) {
            throw new IllegalArgumentException("O número da OS deve ser maior que zero.");
        }
        this.numero = numero;
        this.veiculo = Objects.requireNonNull(veiculo, "O veículo da OS é obrigatório.");
        this.estrategia = Objects.requireNonNull(estrategia, "A estratégia de orçamento é obrigatória.");
    }

    // ------------------------------------------------------------------
    // Itens (Composite)
    // ------------------------------------------------------------------

    /**
     * Adiciona um item à OS. Pode ser um item simples ou um pacote composto.
     *
     * @param item item a adicionar
     */
    public void adicionarItem(ItemOrdemServico item) {
        itens.add(Objects.requireNonNull(item, "O item não pode ser nulo."));
    }

    /**
     * Remove um item de primeiro nível da OS.
     *
     * @param item item a remover
     * @return {@code true} se o item estava na OS e foi removido
     */
    public boolean removerItem(ItemOrdemServico item) {
        return itens.remove(item);
    }

    /**
     * @return visão somente leitura dos itens de primeiro nível da OS
     */
    public List<ItemOrdemServico> getItens() {
        return Collections.unmodifiableList(itens);
    }

    /**
     * Soma o valor de todos os itens, sem aplicar nenhuma regra de orçamento.
     *
     * <p>Cada item calcula o próprio valor: um pacote soma recursivamente seus
     * componentes, então esta classe não precisa conhecer a estrutura interna.</p>
     *
     * @return soma integral dos itens
     */
    public double calcularSubtotal() {
        double subtotal = 0.0;
        for (ItemOrdemServico item : itens) {
            subtotal += item.calcularValor();
        }
        return subtotal;
    }

    // ------------------------------------------------------------------
    // Orçamento (Strategy)
    // ------------------------------------------------------------------

    /**
     * Troca a regra de cálculo do orçamento em tempo de execução.
     *
     * @param novaEstrategia nova regra de orçamento
     */
    public void trocarEstrategia(EstrategiaOrcamento novaEstrategia) {
        this.estrategia = Objects.requireNonNull(novaEstrategia, "A estratégia de orçamento é obrigatória.");
    }

    /**
     * @return estratégia de orçamento em uso
     */
    public EstrategiaOrcamento getEstrategia() {
        return estrategia;
    }

    /**
     * Calcula o total final delegando à estratégia de orçamento em uso.
     *
     * @return valor final do orçamento
     */
    public double calcularTotal() {
        return estrategia.calcularTotal(this);
    }

    // ------------------------------------------------------------------
    // Dados da OS
    // ------------------------------------------------------------------

    public int getNumero() {
        return numero;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    @Override
    public String toString() {
        return String.format("OS #%d | %s | %d item(ns) | Estratégia: %s",
                numero, veiculo, itens.size(), estrategia.getNomeEstrategia());
    }
}
