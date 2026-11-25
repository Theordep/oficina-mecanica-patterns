package br.com.oficina.strategy;

import br.com.oficina.composite.ItemSimples;
import br.com.oficina.composite.PacoteServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstrategiaOrcamentoTest {

    private static final double TOLERANCIA = 0.001;

    private OrdemServico os;

    @BeforeEach
    void montarOrdemComPacoteAninhado() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(new ItemSimples("Pastilha", ItemSimples.Tipo.PECA, 50.0, 2));

        PacoteServico revisao = new PacoteServico("Revisão");
        revisao.adicionarItem(freios);
        revisao.adicionarItem(new ItemSimples("Mão de obra", ItemSimples.Tipo.SERVICO, 60.0, 1));

        os = new OrdemServico(1, new Veiculo("ABC-1D23", "Fiat Argo", 2021, "João Silva"));
        os.adicionarItem(revisao);
        os.adicionarItem(new ItemSimples("Óleo", ItemSimples.Tipo.PECA, 40.0, 1));
    }

    @Test
    void padraoCobraOSubtotalIncluindoPacotesAninhados() {
        assertEquals(200.0, os.calcularTotal(), TOLERANCIA);
    }

    @Test
    void trocarEstrategiaMudaOTotalSemMudarOSubtotal() {
        os.trocarEstrategia(new OrcamentoComDesconto(10.0));

        assertEquals(180.0, os.calcularTotal(), TOLERANCIA);
        assertEquals(200.0, os.calcularSubtotal(), TOLERANCIA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 100.0})
    void aceitaOsLimitesDoPercentual(double percentual) {
        os.trocarEstrategia(new OrcamentoComDesconto(percentual));

        assertEquals(200.0 - 2 * percentual, os.calcularTotal(), TOLERANCIA);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.01, 100.01, Double.NaN, Double.POSITIVE_INFINITY})
    void rejeitaPercentualInvalido(double percentual) {
        assertThrows(IllegalArgumentException.class, () -> new OrcamentoComDesconto(percentual));
    }

    @Test
    void nomesDasEstrategias() {
        assertEquals("Orçamento Padrão", new OrcamentoPadrao().getNomeEstrategia());
        assertEquals("Orçamento com Desconto (12,5%)", new OrcamentoComDesconto(12.5).getNomeEstrategia());
    }
}
