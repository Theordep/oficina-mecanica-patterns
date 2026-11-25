package br.com.oficina.composite;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PacoteServicoTest {

    @Test
    void rejeitaPacoteDentroDeSiMesmo() {
        PacoteServico pacote = new PacoteServico("Revisão");

        assertThrows(IllegalArgumentException.class, () -> pacote.adicionarItem(pacote));
    }

    @Test
    void rejeitaCicloIndireto() {
        PacoteServico revisao = new PacoteServico("Revisão");
        PacoteServico freios = new PacoteServico("Freios");
        PacoteServico fluidos = new PacoteServico("Fluidos");
        revisao.adicionarItem(freios);
        freios.adicionarItem(fluidos);

        assertThrows(IllegalArgumentException.class, () -> fluidos.adicionarItem(revisao));
    }
}
