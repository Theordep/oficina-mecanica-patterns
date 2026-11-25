package br.com.oficina;

import br.com.oficina.composite.ItemOrdemServico;
import br.com.oficina.composite.ItemSimples;
import br.com.oficina.composite.PacoteServico;
import br.com.oficina.model.OrdemServico;
import br.com.oficina.model.Veiculo;
import br.com.oficina.strategy.OrcamentoComDesconto;

import java.util.Locale;

/**
 * Demonstra o fluxo completo: abertura da ordem de serviço, montagem dos itens
 * (Composite) e troca da regra de orçamento em tempo de execução (Strategy).
 */
public class Main {

    public static void main(String[] args) {
        Veiculo veiculo = new Veiculo("ABC-1D23", "Fiat Argo", 2021, "João Silva");
        OrdemServico os = new OrdemServico(1, veiculo);

        os.adicionarItem(new ItemSimples("Bateria 60Ah", ItemSimples.Tipo.PECA, 420.00, 1));
        os.adicionarItem(new ItemSimples("Alinhamento e balanceamento", ItemSimples.Tipo.SERVICO, 150.00, 1));
        os.adicionarItem(criarPacoteRevisao());

        System.out.println(os);
        System.out.println();
        for (ItemOrdemServico item : os.getItens()) {
            System.out.println(item.exibir(0));
        }
        System.out.println();
        System.out.println("Subtotal: " + formatarMoeda(os.calcularSubtotal()));
        exibirTotal(os);

        os.trocarEstrategia(new OrcamentoComDesconto(10.0));
        exibirTotal(os);
    }

    private static PacoteServico criarPacoteRevisao() {
        PacoteServico freios = new PacoteServico("Freios");
        freios.adicionarItem(new ItemSimples("Pastilha de freio", ItemSimples.Tipo.PECA, 89.90, 2));
        freios.adicionarItem(new ItemSimples("Troca das pastilhas", ItemSimples.Tipo.SERVICO, 120.00, 1));

        PacoteServico revisao = new PacoteServico("Revisão de 30.000 km");
        revisao.adicionarItem(new ItemSimples("Óleo 5W30 (litro)", ItemSimples.Tipo.PECA, 48.50, 4));
        revisao.adicionarItem(new ItemSimples("Filtro de óleo", ItemSimples.Tipo.PECA, 35.00, 1));
        revisao.adicionarItem(freios);
        return revisao;
    }

    private static void exibirTotal(OrdemServico os) {
        System.out.println(os.getEstrategia().getNomeEstrategia() + ": " + formatarMoeda(os.calcularTotal()));
    }

    private static String formatarMoeda(double valor) {
        return String.format(Locale.forLanguageTag("pt-BR"), "R$ %,.2f", valor);
    }
}
