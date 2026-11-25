package br.com.oficina.util;

import java.util.Objects;

/** Pré-condições compartilhadas pelas entidades do domínio. */
public final class Validacoes {

    private Validacoes() {
    }

    /**
     * @return o texto sem espaços nas pontas
     * @throws NullPointerException     se o texto for nulo
     * @throws IllegalArgumentException se o texto estiver em branco
     */
    public static String textoObrigatorio(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " não pode ser nulo.");
        if (valor.isBlank()) {
            throw new IllegalArgumentException(campo + " não pode ser vazio.");
        }
        return valor.strip();
    }
}
