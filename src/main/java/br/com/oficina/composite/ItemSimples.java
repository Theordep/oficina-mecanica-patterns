package br.com.oficina.composite;

import br.com.oficina.util.Moeda;
import br.com.oficina.util.Validacoes;

import java.math.BigDecimal;
import java.util.Objects;

/** Papel <i>Leaf</i>: uma peça ou um serviço cobrado por unidade. */
public class ItemSimples implements ItemOrdemServico {

    public enum Tipo {
        PECA,
        SERVICO
    }

    private final String nome;
    private final Tipo tipo;
    private final BigDecimal valorUnitario;
    private final int quantidade;

    /**
     * @param valorUnitario valor em reais, não negativo e com no máximo duas casas decimais
     * @param quantidade    maior que zero
     */
    public ItemSimples(String nome, Tipo tipo, BigDecimal valorUnitario, int quantidade) {
        this.nome = Validacoes.textoObrigatorio(nome, "O nome do item");
        this.tipo = Objects.requireNonNull(tipo, "O tipo do item não pode ser nulo.");
        Objects.requireNonNull(valorUnitario, "O valor unitário não pode ser nulo.");
        if (valorUnitario.signum() < 0 || valorUnitario.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(
                    "O valor unitário deve ser não negativo e ter no máximo duas casas decimais.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.valorUnitario = Moeda.arredondar(valorUnitario);
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public BigDecimal calcularValor() {
        return valorUnitario.multiply(BigDecimal.valueOf(quantidade));
    }

    @Override
    public String exibir(int nivel) {
        return ItemOrdemServico.indentacao(nivel)
                + "- " + nome + " (" + tipo + ") x" + quantidade + " = " + Moeda.formatar(calcularValor());
    }
}
