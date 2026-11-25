package br.com.oficina.composite;

import br.com.oficina.util.Moeda;

import java.math.BigDecimal;

/** Pacote que inclui os serviços e cobra apenas as peças, inclusive em subpacotes. */
public final class PacoteComMaoDeObraInclusa extends PacoteServico {

    public PacoteComMaoDeObraInclusa(String nome) {
        super(nome);
    }

    @Override
    public BigDecimal calcularValor() {
        return super.calcularValor().subtract(super.calcularValorMaoDeObra());
    }

    @Override
    public BigDecimal calcularValorMaoDeObra() {
        return Moeda.ZERO;
    }

    @Override
    protected String rotuloExibicao() {
        return "Pacote com mão de obra inclusa";
    }
}
