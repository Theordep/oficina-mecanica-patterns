package br.com.oficina.composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PacoteServico implements ItemOrdemServico {
    private final String nome;
    private final List<ItemOrdemServico> itens = new ArrayList<>();

    public PacoteServico(String nome) {
        Objects.requireNonNull(nome, "O nome do pacote não pode ser nulo.");
        if (nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do pacote não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public void adicionarItem(ItemOrdemServico item) {
        itens.add(Objects.requireNonNull(item, "O item do pacote não pode ser nulo."));
    }

    public boolean removerItem(ItemOrdemServico item) {
        return itens.remove(item);
    }

    public List<ItemOrdemServico> getItens() {
        return Collections.unmodifiableList(itens);
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public double calcularValor() {
        double total = 0.0;
        for (ItemOrdemServico item : itens) {
            total += item.calcularValor();
        }
        return total;
    }

    private static String repetirEspacos(int nivel) {
        StringBuilder espacos = new StringBuilder();
        for (int i = 0; i < nivel; i++) {
            espacos.append("  ");
        }
        return espacos.toString();
    }

    @Override
    public String exibir(int nivel) {
        if (nivel < 0) {
            throw new IllegalArgumentException("O nível não pode ser negativo.");
        }
        String indentacao = repetirEspacos(nivel);
        String valor = String.format(Locale.forLanguageTag("pt-BR"), "R$ %,.2f", calcularValor());
        StringBuilder exibicao = new StringBuilder()
                .append(indentacao).append("- Pacote: ").append(nome).append(" = ").append(valor);

        for (ItemOrdemServico item : itens) {
            exibicao.append(System.lineSeparator()).append(item.exibir(nivel + 1));
        }
        return exibicao.toString();
    }
}
