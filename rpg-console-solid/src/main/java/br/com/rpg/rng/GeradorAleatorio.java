package br.com.rpg.rng;

/**
 * Princípio da Inversão de Dependência (DIP):
 * Módulos de alto nível (MotorCombate, GerenciadorEventos, GeradorLoot) dependem desta abstração
 * e não diretamente de java.util.Random, permitindo determinismo em testes unitários.
 */
public interface GeradorAleatorio {
    int proximoInt(int minInclusivo, int maxInclusivo);
    double proximaProbabilidade();
    boolean testarChance(double probabilidadePercentual);
    boolean testarChanceComSorte(double chanceBasePercentual, int sorte);
}
