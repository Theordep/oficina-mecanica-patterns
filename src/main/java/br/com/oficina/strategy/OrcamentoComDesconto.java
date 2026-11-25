package br.com.oficina.strategy;

import br.com.oficina.model.OrdemServico;
import br.com.oficina.util.Moeda;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

/**
 * <i>ConcreteStrategy</i>: aplica um desconto percentual sobre o subtotal da ordem de serviço.
 */
public class OrcamentoComDesconto implements EstrategiaOrcamento {

    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final BigDecimal percentualDesconto;

    /**
     * @param percentualDesconto desconto em pontos percentuais, de 0 a 100 ({@code 10} = 10%)
     * @throws IllegalArgumentException se o percentual estiver fora do intervalo
     */
    public OrcamentoComDesconto(BigDecimal percentualDesconto) {
        Objects.requireNonNull(percentualDesconto, "O percentual de desconto é obrigatório.");
        if (percentualDesconto.signum() < 0 || percentualDesconto.compareTo(CEM) > 0) {
            throw new IllegalArgumentException("O percentual de desconto deve estar entre 0 e 100.");
        }
        this.percentualDesconto = percentualDesconto;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    /** O desconto é arredondado para centavos antes de ser subtraído. */
    @Override
    public BigDecimal calcularTotal(OrdemServico os) {
        Objects.requireNonNull(os, "A ordem de serviço é obrigatória.");
        BigDecimal subtotal = os.calcularSubtotal();
        BigDecimal desconto = Moeda.arredondar(subtotal.multiply(percentualDesconto).movePointLeft(2));
        return subtotal.subtract(desconto);
    }

    @Override
    public String getNomeEstrategia() {
        DecimalFormat formato = new DecimalFormat(
                "0.0#", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("pt-BR")));
        return "Orçamento com Desconto (" + formato.format(percentualDesconto) + "%)";
    }
}
