package br.com.oficina.model;

import java.util.Objects;

public class Veiculo {
    private final String placa;
    private final String modelo;
    private final int ano;
    private final String proprietario;

    public Veiculo(String placa, String modelo, int ano, String proprietario) {
        this.placa = validarTexto(placa, "A placa");
        this.modelo = validarTexto(modelo, "O modelo");
        if (ano <= 0) {
            throw new IllegalArgumentException("O ano deve ser maior que zero.");
        }
        this.ano = ano;
        this.proprietario = validarTexto(proprietario, "O proprietário");
    }

    private static String validarTexto(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " não pode ser nulo.");
        if (valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return valor.trim();
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
