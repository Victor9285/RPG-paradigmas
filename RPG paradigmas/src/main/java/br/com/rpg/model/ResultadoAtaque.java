package br.com.rpg.model;

public record ResultadoAtaque(
        String atacante,
        String alvo,
        int danoCausado,
        boolean esquivou,
        boolean critico,
        String descricao
) {
}
