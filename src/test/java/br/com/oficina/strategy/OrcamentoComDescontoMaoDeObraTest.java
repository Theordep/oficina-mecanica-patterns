package br.com.oficina.strategy;

import br.com.oficina.composite.ItemSimples;
import br.com.oficina.composite.PacoteComMaoDeObraInclusa;
import br.com.oficina.composite.PacoteServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static br.com.oficina.composite.ItemSimples.Tipo.PECA;
import static br.com.oficina.composite.ItemSimples.Tipo.SERVICO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrcamentoComDescontoMaoDeObraTest {

    private static final BigDecimal SUBTOTAL = new BigDecimal("200.00");

    private OrdemServico os;

    /**
     * Subtotal de R$ 200,00, com R$ 80,00 de mão de obra cobrada:
     * <pre>
     * - Óleo (PECA)                         40,00
     * - Pacote Revisão                     130,00
     *   - Pacote Freios                     80,00
     *     - Pastilha (PECA)                 50,00
     *     - Troca das pastilhas (SERVICO)   30,00  &lt;- cobrada
     *   - Alinhamento (SERVICO)             50,00  &lt;- cobrada
     * - Pacote Inspeção (mão de obra inclusa) 30,00
     *   - Filtro (PECA)                     30,00
     *   - Inspeção visual (SERVICO)         45,00  &lt;- inclusa, não cobrada
     * </pre>
     */
    @BeforeEach
    void montarOrdem() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(item("Pastilha", PECA, "50.00"));
        freios.adicionarItem(item("Troca das pastilhas", SERVICO, "30.00"));

        PacoteServico revisao = new PacoteServico("Revisão");
        revisao.adicionarItem(freios);
        revisao.adicionarItem(item("Alinhamento", SERVICO, "50.00"));

        PacoteServico inspecao = new PacoteComMaoDeObraInclusa("Inspeção");
        inspecao.adicionarItem(item("Filtro", PECA, "30.00"));
        inspecao.adicionarItem(item("Inspeção visual", SERVICO, "45.00"));

        os = novaOrdem();
        os.adicionarItem(item("Óleo", PECA, "40.00"));
        os.adicionarItem(revisao);
        os.adicionarItem(inspecao);
    }

    private static OrdemServico novaOrdem() {
        return new OrdemServico(1, new Veiculo("ABC-1D23", "Fiat Argo", 2021, "João Silva"));
    }

    private static ItemSimples item(String nome, ItemSimples.Tipo tipo, String valor) {
        return new ItemSimples(nome, tipo, new BigDecimal(valor), 1);
    }

    private static OrcamentoComDescontoMaoDeObra desconto(String percentual) {
        return new OrcamentoComDescontoMaoDeObra(new BigDecimal(percentual));
    }

    @Test
    void descontaVinteECincoPorCentoDaMaoDeObraCobrada() {
        os.trocarEstrategia(desconto("25"));

        assertEquals(SUBTOTAL, os.calcularSubtotal());
        assertEquals(new BigDecimal("180.00"), os.calcularTotal());
    }

    @Test
    void trocaDeEstrategiaNaMesmaOrdemNaoAlteraOSubtotal() {
        assertEquals(SUBTOTAL, os.calcularTotal());

        OrcamentoComDescontoMaoDeObra descontoMaoDeObra = desconto("25");
        os.trocarEstrategia(descontoMaoDeObra);
        assertSame(descontoMaoDeObra, os.getEstrategia());
        assertEquals(new BigDecimal("180.00"), os.calcularTotal());

        os.trocarEstrategia(new OrcamentoComDesconto(new BigDecimal("25")));
        assertEquals(new BigDecimal("150.00"), os.calcularTotal());

        os.trocarEstrategia(new OrcamentoPadrao());
        assertEquals(SUBTOTAL, os.calcularTotal());

        assertEquals(SUBTOTAL, os.calcularSubtotal());
    }

    @Test
    void consideraMaoDeObraEmPacotesAninhadosEmVariosNiveis() {
        PacoteServico interno = new PacoteServico("Suspensão");
        interno.adicionarItem(item("Troca do amortecedor", SERVICO, "100.00"));
        PacoteServico meio = new PacoteServico("Revisão completa");
        meio.adicionarItem(interno);
        PacoteServico externo = new PacoteServico("Programa de manutenção");
        externo.adicionarItem(meio);

        OrdemServico ordem = novaOrdem();
        ordem.adicionarItem(externo);
        ordem.trocarEstrategia(desconto("10"));

        assertEquals(new BigDecimal("90.00"), ordem.calcularTotal());
    }

    @Test
    void naoDescontaMaoDeObraInclusaMesmoDentroDePacoteComum() {
        PacoteServico cortesia = new PacoteComMaoDeObraInclusa("Cortesia");
        cortesia.adicionarItem(item("Lâmpada", PECA, "20.00"));
        cortesia.adicionarItem(item("Troca da lâmpada", SERVICO, "15.00"));

        PacoteServico revisao = new PacoteServico("Revisão");
        revisao.adicionarItem(cortesia);
        revisao.adicionarItem(item("Diagnóstico", SERVICO, "40.00"));

        OrdemServico ordem = novaOrdem();
        ordem.adicionarItem(revisao);
        ordem.trocarEstrategia(desconto("50"));

        // subtotal 60,00 (20,00 de peça + 40,00 de diagnóstico); desconto só sobre os 40,00
        assertEquals(new BigDecimal("60.00"), ordem.calcularSubtotal());
        assertEquals(new BigDecimal("40.00"), ordem.calcularTotal());
    }

    @Test
    void ordemSemMaoDeObraCobradaNaoRecebeDesconto() {
        PacoteServico inclusa = new PacoteComMaoDeObraInclusa("Troca de óleo");
        inclusa.adicionarItem(item("Óleo", PECA, "80.00"));
        inclusa.adicionarItem(item("Troca do óleo", SERVICO, "40.00"));

        OrdemServico ordem = novaOrdem();
        ordem.adicionarItem(inclusa);
        ordem.adicionarItem(item("Filtro", PECA, "20.00"));
        ordem.trocarEstrategia(desconto("100"));

        assertEquals(new BigDecimal("100.00"), ordem.calcularTotal());
    }

    @Test
    void ordemVaziaTotalizaZero() {
        OrdemServico ordem = novaOrdem();
        ordem.trocarEstrategia(desconto("25"));

        assertEquals(new BigDecimal("0.00"), ordem.calcularTotal());
    }

    @ParameterizedTest
    @CsvSource({"0, 200.00", "12.5, 190.00", "25, 180.00", "100, 120.00"})
    void aplicaPercentuaisDentroDoIntervaloInclusive(BigDecimal percentual, BigDecimal esperado) {
        os.trocarEstrategia(new OrcamentoComDescontoMaoDeObra(percentual));

        assertEquals(esperado, os.calcularTotal());
        assertEquals(SUBTOTAL, os.calcularSubtotal());
    }

    @Test
    void arredondaODescontoParaCentavosComHalfUp() {
        OrdemServico ordem = novaOrdem();
        ordem.adicionarItem(item("Regulagem", SERVICO, "10.10"));
        ordem.trocarEstrategia(desconto("5"));

        // 5% de 10,10 = 0,505, arredondado para 0,51
        assertEquals(new BigDecimal("9.59"), ordem.calcularTotal());
    }

    @Test
    void arredondaParaBaixoQuandoAbaixoDoMeioCentavo() {
        os.trocarEstrategia(desconto("33.33"));

        // 33,33% de 80,00 = 26,664, arredondado para 26,66
        assertEquals(new BigDecimal("173.34"), os.calcularTotal());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "100.01", "-100", "1000"})
    void rejeitaPercentualForaDoIntervalo(BigDecimal percentual) {
        assertThrows(IllegalArgumentException.class, () -> new OrcamentoComDescontoMaoDeObra(percentual));
    }

    @Test
    void rejeitaPercentualNulo() {
        assertThrows(NullPointerException.class, () -> new OrcamentoComDescontoMaoDeObra(null));
    }

    @Test
    void rejeitaOrdemNula() {
        assertThrows(NullPointerException.class, () -> desconto("10").calcularTotal(null));
    }

    @Test
    void nomeDaEstrategia() {
        assertEquals("Orçamento com Desconto na Mão de Obra (25,0%)", desconto("25").getNomeEstrategia());
        assertEquals("Orçamento com Desconto na Mão de Obra (12,5%)", desconto("12.5").getNomeEstrategia());
    }
}
