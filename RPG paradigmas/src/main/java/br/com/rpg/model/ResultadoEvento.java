package br.com.rpg.model;

public record ResultadoEvento(
        String tituloEvento,
        String narrativa,
        boolean positivo,
        int variacaoVida,
        int variacaoOuro,
        int variacaoXp,
        int variacaoSorte
) {
}
