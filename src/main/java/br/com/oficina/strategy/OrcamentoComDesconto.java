package br.com.oficina.strategy;

import br.com.oficina.model.OrdemServico;

import java.util.Locale;
import java.util.Objects;

/**
 * <i>ConcreteStrategy</i>: aplica um desconto percentual sobre o subtotal da ordem de serviço.
 */
public class OrcamentoComDesconto implements EstrategiaOrcamento {

    private final double percentualDesconto;

    /**
     * @param percentualDesconto desconto em pontos percentuais, de 0 a 100 ({@code 10.0} = 10%)
     * @throws IllegalArgumentException se o percentual estiver fora do intervalo ou não for um número
     */
    public OrcamentoComDesconto(double percentualDesconto) {
        if (!Double.isFinite(percentualDesconto) || percentualDesconto < 0 || percentualDesconto > 100) {
            throw new IllegalArgumentException("O percentual de desconto deve estar entre 0 e 100.");
        }
        this.percentualDesconto = percentualDesconto;
    }

    public double getPercentualDesconto() {
        return percentualDesconto;
    }

    @Override
    public double calcularTotal(OrdemServico os) {
        Objects.requireNonNull(os, "A ordem de serviço é obrigatória.");
        double subtotal = os.calcularSubtotal();
        return subtotal - subtotal * percentualDesconto / 100;
    }

    @Override
    public String getNomeEstrategia() {
        return String.format(Locale.forLanguageTag("pt-BR"),
                "Orçamento com Desconto (%.1f%%)", percentualDesconto);
    }
}
