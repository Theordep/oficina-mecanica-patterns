package br.com.oficina.strategy;

import br.com.oficina.model.OrdemServico;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <i>ConcreteStrategy</i> padrão: cobra a soma integral dos itens, sem ajustes.
 */
public class OrcamentoPadrao implements EstrategiaOrcamento {

    @Override
    public BigDecimal calcularTotal(OrdemServico os) {
        Objects.requireNonNull(os, "A ordem de serviço é obrigatória.");
        return os.calcularSubtotal();
    }

    @Override
    public String getNomeEstrategia() {
        return "Orçamento Padrão";
    }
}
