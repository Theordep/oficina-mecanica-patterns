package br.com.oficina.composite;

import br.com.oficina.util.Moeda;
import br.com.oficina.util.Validacoes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Papel <i>Composite</i>: agrupa itens simples e outros pacotes.
 *
 * <p>Pode ser estendida para criar novos tipos de pacote.</p>
 */
public class PacoteServico implements ItemOrdemServico {

    private final String nome;
    private final List<ItemOrdemServico> itens = new ArrayList<>();

    public PacoteServico(String nome) {
        this.nome = Validacoes.textoObrigatorio(nome, "O nome do pacote");
    }

    /**
     * @throws IllegalArgumentException se a inclusão formar um ciclo na árvore
     */
    public void adicionarItem(ItemOrdemServico item) {
        Objects.requireNonNull(item, "O item do pacote não pode ser nulo.");
        if (item == this || item.contem(this)) {
            throw new IllegalArgumentException("Um pacote não pode conter a si mesmo.");
        }
        itens.add(item);
    }

    public boolean removerItem(ItemOrdemServico item) {
        return itens.remove(item);
    }

    /** @return visão somente leitura dos componentes diretos */
    public List<ItemOrdemServico> getItens() {
        return Collections.unmodifiableList(itens);
    }

    @Override
    public boolean contem(ItemOrdemServico alvo) {
        return itens.stream().anyMatch(item -> item == alvo || item.contem(alvo));
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public BigDecimal calcularValor() {
        return itens.stream()
                .map(ItemOrdemServico::calcularValor)
                .reduce(Moeda.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calcularValorMaoDeObra() {
        return itens.stream()
                .map(ItemOrdemServico::calcularValorMaoDeObra)
                .reduce(Moeda.ZERO, BigDecimal::add);
    }

    @Override
    public String exibir(int nivel) {
        StringBuilder exibicao = new StringBuilder(ItemOrdemServico.indentacao(nivel))
                .append("- ").append(rotuloExibicao()).append(": ").append(nome)
                .append(" = ").append(Moeda.formatar(calcularValor()));
        for (ItemOrdemServico item : itens) {
            exibicao.append(System.lineSeparator()).append(item.exibir(nivel + 1));
        }
        return exibicao.toString();
    }

    protected String rotuloExibicao() {
        return "Pacote";
    }
}
