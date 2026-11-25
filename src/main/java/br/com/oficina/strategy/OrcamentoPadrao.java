package br.com.oficina.strategy;

import br.com.oficina.model.OrdemServico;

import java.util.Objects;

/**
 * <i>ConcreteStrategy</i> padrão: cobra a soma integral dos itens, sem ajustes.
 */
public class OrcamentoPadrao implements EstrategiaOrcamento {

    @Override
    public double calcularTotal(OrdemServico os) {
        Objects.requireNonNull(os, "A ordem de serviço é obrigatória.");
        return os.calcularSubtotal();
    }

    @Override
    public String getNomeEstrategia() {
        return "Orçamento Padrão";
    }
}
