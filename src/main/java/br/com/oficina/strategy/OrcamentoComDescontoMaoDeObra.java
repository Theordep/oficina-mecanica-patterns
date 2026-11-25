package br.com.oficina.strategy;

import br.com.oficina.composite.ItemOrdemServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.util.Moeda;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;

/**
 * <i>ConcreteStrategy</i>: aplica um desconto percentual apenas sobre a mão de obra cobrada na OS.
 *
 * <p>A base do desconto é a soma de {@link ItemOrdemServico#calcularValorMaoDeObra()} dos itens de
 * primeiro nível. Os pacotes já percorrem seus subpacotes, e a mão de obra de um pacote com mão de
 * obra inclusa vale zero, então ela não é descontada novamente.</p>
 */
public class OrcamentoComDescontoMaoDeObra implements EstrategiaOrcamento {

    private static final BigDecimal CEM = BigDecimal.valueOf(100);

    private final BigDecimal percentualDesconto;

    /**
     * @param percentualDesconto desconto em pontos percentuais, de 0 a 100 ({@code 25} = 25%)
     * @throws IllegalArgumentException se o percentual estiver fora do intervalo
     */
    public OrcamentoComDescontoMaoDeObra(BigDecimal percentualDesconto) {
        Objects.requireNonNull(percentualDesconto, "O percentual de desconto é obrigatório.");
        if (percentualDesconto.signum() < 0 || percentualDesconto.compareTo(CEM) > 0) {
            throw new IllegalArgumentException("O percentual de desconto deve estar entre 0 e 100.");
        }
        this.percentualDesconto = percentualDesconto;
    }

    public BigDecimal getPercentualDesconto() {
        return percentualDesconto;
    }

    /** O desconto é calculado sobre a mão de obra cobrada e arredondado para centavos. */
    @Override
    public BigDecimal calcularTotal(OrdemServico os) {
        Objects.requireNonNull(os, "A ordem de serviço é obrigatória.");
        BigDecimal maoDeObra = os.getItens().stream()
                .map(ItemOrdemServico::calcularValorMaoDeObra)
                .reduce(Moeda.ZERO, BigDecimal::add);
        BigDecimal desconto = Moeda.arredondar(maoDeObra.multiply(percentualDesconto).movePointLeft(2));
        return os.calcularSubtotal().subtract(desconto);
    }

    @Override
    public String getNomeEstrategia() {
        DecimalFormat formato = new DecimalFormat(
                "0.0#", DecimalFormatSymbols.getInstance(Locale.forLanguageTag("pt-BR")));
        return "Orçamento com Desconto na Mão de Obra (" + formato.format(percentualDesconto) + "%)";
    }
}
