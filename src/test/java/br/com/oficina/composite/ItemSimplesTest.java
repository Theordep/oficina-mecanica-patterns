package br.com.oficina.composite;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemSimplesTest {

    @Test
    void multiplicaSemErroDePontoFlutuante() {
        ItemSimples item = new ItemSimples("Arruela", ItemSimples.Tipo.PECA, new BigDecimal("0.1"), 3);

        assertEquals(new BigDecimal("0.30"), item.calcularValor());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "1.005"})
    void rejeitaValorInvalido(BigDecimal valor) {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemSimples("Filtro", ItemSimples.Tipo.PECA, valor, 1));
    }

    @Test
    void rejeitaQuantidadeZero() {
        assertThrows(IllegalArgumentException.class,
                () -> new ItemSimples("Filtro", ItemSimples.Tipo.PECA, BigDecimal.ONE, 0));
    }
}
