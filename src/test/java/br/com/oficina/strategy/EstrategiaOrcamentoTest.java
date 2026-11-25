package br.com.oficina.strategy;

import br.com.oficina.composite.ItemSimples;
import br.com.oficina.composite.PacoteServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Veiculo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EstrategiaOrcamentoTest {

    private OrdemServico os;

    @BeforeEach
    void montarOrdemComPacoteAninhado() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(item("Pastilha", "50.00", 2));

        PacoteServico revisao = new PacoteServico("Revisão");
        revisao.adicionarItem(freios);
        revisao.adicionarItem(item("Mão de obra", "60.00", 1));

        os = new OrdemServico(1, new Veiculo("ABC-1D23", "Fiat Argo", 2021, "João Silva"));
        os.adicionarItem(revisao);
        os.adicionarItem(item("Óleo", "40.00", 1));
    }

    private static ItemSimples item(String nome, String valor, int quantidade) {
        return new ItemSimples(nome, ItemSimples.Tipo.PECA, new BigDecimal(valor), quantidade);
    }

    @Test
    void padraoCobraOSubtotalIncluindoPacotesAninhados() {
        assertEquals(new BigDecimal("200.00"), os.calcularTotal());
    }

    @ParameterizedTest
    @CsvSource({"0, 200.00", "10, 180.00", "12.5, 175.00", "100, 0.00"})
    void trocarEstrategiaMudaOTotalSemMudarOSubtotal(BigDecimal percentual, BigDecimal esperado) {
        os.trocarEstrategia(new OrcamentoComDesconto(percentual));

        assertEquals(esperado, os.calcularTotal());
        assertEquals(new BigDecimal("200.00"), os.calcularSubtotal());
    }

    @Test
    void arredondaODescontoParaCentavos() {
        os.adicionarItem(item("Parafuso", "0.05", 1));
        os.trocarEstrategia(new OrcamentoComDesconto(BigDecimal.TEN));

        // 10% de 200,05 = 20,005, arredondado para 20,01
        assertEquals(new BigDecimal("180.04"), os.calcularTotal());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-0.01", "100.01"})
    void rejeitaPercentualForaDoIntervalo(BigDecimal percentual) {
        assertThrows(IllegalArgumentException.class, () -> new OrcamentoComDesconto(percentual));
    }

    @Test
    void nomesDasEstrategias() {
        assertEquals("Orçamento Padrão", new OrcamentoPadrao().getNomeEstrategia());
        assertEquals("Orçamento com Desconto (12,5%)",
                new OrcamentoComDesconto(new BigDecimal("12.5")).getNomeEstrategia());
    }
}
