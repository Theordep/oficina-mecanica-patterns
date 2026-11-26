package br.com.oficina;

import br.com.oficina.composite.PacoteComMaoDeObraInclusa;
import br.com.oficina.strategy.OrcamentoComDescontoMaoDeObra;
import br.com.oficina.composite.ItemOrdemServico;
import br.com.oficina.composite.ItemSimples;
import br.com.oficina.composite.PacoteServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Veiculo;
import br.com.oficina.strategy.OrcamentoComDesconto;
import br.com.oficina.util.Moeda;

import java.math.BigDecimal;

/**
 * Demonstra o fluxo completo: abertura da ordem de serviço, montagem dos itens
 * (Composite) e troca da regra de orçamento em tempo de execução (Strategy).
 */
public class Main {

    public static void main(String[] args) {
        Veiculo veiculo = new Veiculo("ABC-1D23", "Fiat Argo", 2021, "João Silva");
        OrdemServico os = new OrdemServico(1, veiculo);

        os.adicionarItem(new ItemSimples("Bateria 60Ah", ItemSimples.Tipo.PECA, new BigDecimal("420.00"), 1));
        os.adicionarItem(new ItemSimples("Alinhamento e balanceamento", ItemSimples.Tipo.SERVICO, new BigDecimal("150.00"), 1));
        os.adicionarItem(criarPacoteRevisao());

        System.out.println(os);
        System.out.println();
        for (ItemOrdemServico item : os.getItens()) {
            System.out.println(item.exibir(0));
        }
        System.out.println();
        System.out.println("Subtotal: " + Moeda.formatar(os.calcularSubtotal()));
        exibirTotal(os);

        os.trocarEstrategia(new OrcamentoComDesconto(BigDecimal.TEN));
        exibirTotal(os);
        demonstrarParte2();
    }

    private static void demonstrarParte2() {
        OrdemServico os = new OrdemServico(2,
                new Veiculo("DEF-4G56", "Fiat Uno", 2020, "Maria Silva"));
        PacoteServico subpacote = new PacoteServico("Troca de peças");
        subpacote.adicionarItem(new ItemSimples("Peças", ItemSimples.Tipo.PECA, new BigDecimal("100.00"), 1));
        subpacote.adicionarItem(new ItemSimples("Instalação inclusa", ItemSimples.Tipo.SERVICO, new BigDecimal("60.00"), 1));
        PacoteComMaoDeObraInclusa incluso = new PacoteComMaoDeObraInclusa("Manutenção com instalação");
        incluso.adicionarItem(subpacote);
        os.adicionarItem(incluso);
        os.adicionarItem(new ItemSimples("Serviço avulso", ItemSimples.Tipo.SERVICO, new BigDecimal("40.00"), 1));
        PacoteServico comum = new PacoteServico("Manutenção complementar");
        comum.adicionarItem(new ItemSimples("Peça complementar", ItemSimples.Tipo.PECA, new BigDecimal("20.00"), 1));
        comum.adicionarItem(new ItemSimples("Serviço complementar", ItemSimples.Tipo.SERVICO, new BigDecimal("40.00"), 1));
        os.adicionarItem(comum);

        System.out.println("\nParte 2");
        System.out.println(os);
        for (ItemOrdemServico item : os.getItens()) {
            System.out.println(item.exibir(0));
        }
        BigDecimal maoDeObra = os.getItens().stream()
                .map(ItemOrdemServico::calcularValorMaoDeObra)
                .reduce(Moeda.ZERO, BigDecimal::add);
        System.out.println("Subtotal: " + Moeda.formatar(os.calcularSubtotal()));
        System.out.println("Mão de obra cobrada: " + Moeda.formatar(maoDeObra));
        exibirTotal(os);
        os.trocarEstrategia(new OrcamentoComDescontoMaoDeObra(new BigDecimal("25")));
        exibirTotal(os);
    }

    private static PacoteServico criarPacoteRevisao() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(new ItemSimples("Pastilha de freio", ItemSimples.Tipo.PECA, new BigDecimal("89.90"), 2));
        freios.adicionarItem(new ItemSimples("Troca das pastilhas", ItemSimples.Tipo.SERVICO, new BigDecimal("120.00"), 1));

        PacoteServico revisao = new PacoteServico("Revisão de 30.000 km");
        revisao.adicionarItem(new ItemSimples("Óleo 5W30 (litro)", ItemSimples.Tipo.PECA, new BigDecimal("48.50"), 4));
        revisao.adicionarItem(new ItemSimples("Filtro de óleo", ItemSimples.Tipo.PECA, new BigDecimal("35.00"), 1));
        revisao.adicionarItem(freios);
        return revisao;
    }

    private static void exibirTotal(OrdemServico os) {
        System.out.println(os.getEstrategia().getNomeEstrategia() + ": " + Moeda.formatar(os.calcularTotal()));
    }
}
