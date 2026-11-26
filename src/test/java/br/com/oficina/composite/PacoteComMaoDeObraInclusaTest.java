package br.com.oficina.composite;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static br.com.oficina.composite.ItemSimples.Tipo.PECA;
import static br.com.oficina.composite.ItemSimples.Tipo.SERVICO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PacoteComMaoDeObraInclusaTest {

    @Test
    void cobraPecasMesmoQuandoEstaoEmSubpacotes() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(new ItemSimples("Pastilha", PECA, new BigDecimal("50.00"), 2));
        freios.adicionarItem(new ItemSimples("Troca das pastilhas", SERVICO, new BigDecimal("60.00"), 1));

        PacoteServico revisao = new PacoteComMaoDeObraInclusa("Revisão");
        revisao.adicionarItem(freios);
        revisao.adicionarItem(new ItemSimples("Diagnóstico", SERVICO, new BigDecimal("25.00"), 1));

        assertEquals(new BigDecimal("100.00"), revisao.calcularValor());
    }

    @Test
    void pacoteComumSomaSoAMaoDeObraCobradaNosSubpacotes() {
        PacoteServico alinhamento = new PacoteServico("Alinhamento");
        alinhamento.adicionarItem(new ItemSimples("Parafuso", PECA, new BigDecimal("20.00"), 1));
        alinhamento.adicionarItem(new ItemSimples("Alinhamento", SERVICO, new BigDecimal("20.00"), 2));

        PacoteServico cortesia = new PacoteComMaoDeObraInclusa("Inspeção");
        cortesia.adicionarItem(new ItemSimples("Inspeção visual", SERVICO, new BigDecimal("60.00"), 1));

        PacoteServico ordem = new PacoteServico("Revisão");
        ordem.adicionarItem(alinhamento);
        ordem.adicionarItem(cortesia);
        ordem.adicionarItem(new ItemSimples("Diagnóstico", SERVICO, new BigDecimal("40.00"), 1));

        assertEquals(new BigDecimal("80.00"), ordem.calcularValorMaoDeObra());
    }

    @Test
    void pacotesComMaoDeObraInclusaAninhadosCobramAsPecasUmaVez() {
        PacoteServico compostoInterno = new PacoteComMaoDeObraInclusa("Freios");
        compostoInterno.adicionarItem(new ItemSimples("Pastilha", PECA, new BigDecimal("100.00"), 1));
        compostoInterno.adicionarItem(new ItemSimples("Troca das pastilhas", SERVICO, new BigDecimal("60.00"), 1));

        PacoteServico revisao = new PacoteComMaoDeObraInclusa("Revisão");
        revisao.adicionarItem(compostoInterno);

        assertEquals(new BigDecimal("100.00"), revisao.calcularValor());
    }

    @Test
    void identificaAMaoDeObraInclusaNaExibicao() {
        PacoteServico cortesia = new PacoteComMaoDeObraInclusa("Inspeção");
        cortesia.adicionarItem(new ItemSimples("Inspeção visual", SERVICO, new BigDecimal("60.00"), 1));

        String exibicao = cortesia.exibir(0);

        assertTrue(exibicao.contains("mão de obra inclusa: Inspeção = R$ 0,00"));
        assertTrue(exibicao.contains("  - Inspeção visual"));
    }

    @Test
    void rejeitaCiclosEntrePacotesComunsEComMaoDeObraInclusa() {
        PacoteServico revisao = new PacoteComMaoDeObraInclusa("Revisão");
        PacoteServico freios = new PacoteServico("Freios");
        revisao.adicionarItem(freios);

        assertThrows(IllegalArgumentException.class, () -> freios.adicionarItem(revisao));
    }
}
