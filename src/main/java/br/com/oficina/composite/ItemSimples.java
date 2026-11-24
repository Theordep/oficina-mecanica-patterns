package br.com.oficina.composite;

import java.util.Locale;
import java.util.Objects;

public class ItemSimples implements ItemOrdemServico {
    public enum Tipo {
        PECA,
        SERVICO
    }

    private final String nome;
    private final Tipo tipo;
    private final double valorUnitario;
    private final int quantidade;

    public ItemSimples(String nome, Tipo tipo, double valorUnitario, int quantidade) {
        Objects.requireNonNull(nome, "O nome do item não pode ser nulo.");
        if (nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do item não pode ser vazio.");
        }
        this.nome = nome.trim();
        this.tipo = Objects.requireNonNull(tipo, "O tipo do item não pode ser nulo.");
        if (!Double.isFinite(valorUnitario) || valorUnitario < 0) {
            throw new IllegalArgumentException("O valor unitário não pode ser negativo ou inválido.");
        }
        if (quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        this.valorUnitario = valorUnitario;
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public int getQuantidade() {
        return quantidade;
    }

    @Override
    public String getDescricao() {
        return nome;
    }

    @Override
    public double calcularValor() {
        return valorUnitario * quantidade;
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
        return indentacao + "- " + nome + " (" + tipo + ") x" + quantidade + " = " + valor;
    }
}
