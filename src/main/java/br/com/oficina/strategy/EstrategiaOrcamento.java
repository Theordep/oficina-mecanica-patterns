package br.com.oficina.strategy;

import br.com.oficina.model.OrdemServico;

import java.math.BigDecimal;

/**
 * Papel <i>Strategy</i>: regra de cálculo do valor final de uma {@link OrdemServico}.
 *
 * <p>Uma nova regra de orçamento é uma nova implementação desta interface;
 * a ordem de serviço não precisa ser alterada.</p>
 */
public interface EstrategiaOrcamento {

    /**
     * Calcula o valor final da ordem de serviço, sem modificá-la.
     *
     * <p>Implementações devem partir de {@link OrdemServico#calcularSubtotal()} ou
     * {@link OrdemServico#getItens()}. Chamar {@link OrdemServico#calcularTotal()}
     * causaria recursão infinita, pois esse método delega para a própria estratégia.</p>
     *
     * @param os ordem de serviço a orçar
     * @return valor final do orçamento
     */
    BigDecimal calcularTotal(OrdemServico os);

    /**
     * @return nome legível da regra, para exibição
     */
    String getNomeEstrategia();
}
