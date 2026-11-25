package br.com.oficina.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/** Regras monetárias do sistema: valores em reais, sempre com duas casas decimais. */
public final class Moeda {

    private static final int CASAS = 2;
    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    /** Zero já na escala monetária, para servir de ponto de partida em somas. */
    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(CASAS);

    private Moeda() {
    }

    /** Arredonda um resultado de cálculo para centavos ({@link RoundingMode#HALF_UP}). */
    public static BigDecimal arredondar(BigDecimal valor) {
        return valor.setScale(CASAS, RoundingMode.HALF_UP);
    }

    public static String formatar(BigDecimal valor) {
        // NumberFormat não é thread-safe e usa espaço não separável, que alguns consoles exibem mal.
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor).replace('\u00a0', ' ');
    }
}
