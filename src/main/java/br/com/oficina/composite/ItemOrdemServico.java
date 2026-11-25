package br.com.oficina.composite;

import br.com.oficina.util.Moeda;

import java.math.BigDecimal;

/** Papel <i>Component</i>: contrato comum a itens simples e pacotes. */
public interface ItemOrdemServico {

    /** @return nome legível do item ou pacote */
    String getDescricao();

    /** @return valor total; em um pacote, a soma recursiva dos componentes */
    BigDecimal calcularValor();

    /**
     * @return parcela de mão de obra cobrada neste item ou pacote.
     * Itens que cobram mão de obra devem sobrescrever este método.
     */
    default BigDecimal calcularValorMaoDeObra() {
        return Moeda.ZERO;
    }

    /**
     * @param nivel profundidade na árvore, usada na indentação (zero ou maior)
     * @return representação textual do item e de seus componentes
     */
    String exibir(int nivel);

    /**
     * Indica se {@code alvo} está em qualquer nível abaixo deste item. Folhas não contêm nada.
     * Todo composto deve sobrescrever, para que ciclos possam ser detectados.
     */
    default boolean contem(ItemOrdemServico alvo) {
        return false;
    }

    /** @return prefixo de indentação correspondente ao nível */
    static String indentacao(int nivel) {
        if (nivel < 0) {
            throw new IllegalArgumentException("O nível não pode ser negativo.");
        }
        return "  ".repeat(nivel);
    }
}
