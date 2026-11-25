package br.com.oficina.model;

import br.com.oficina.util.Validacoes;

public class Veiculo {
    private final String placa;
    private final String modelo;
    private final int ano;
    private final String proprietario;

    public Veiculo(String placa, String modelo, int ano, String proprietario) {
        this.placa = Validacoes.textoObrigatorio(placa, "A placa");
        this.modelo = Validacoes.textoObrigatorio(modelo, "O modelo");
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser maior que zero.");
        }
        this.ano = ano;
        this.proprietario = Validacoes.textoObrigatorio(proprietario, "O proprietário");
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAno() {
        return ano;
    }

    public String getProprietario() {
        return proprietario;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%d) - Prop.: %s", placa, modelo, ano, proprietario);
    }
}
