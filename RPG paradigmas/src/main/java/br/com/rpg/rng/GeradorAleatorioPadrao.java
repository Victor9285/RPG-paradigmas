package br.com.rpg.rng;

import java.util.concurrent.ThreadLocalRandom;

public class GeradorAleatorioPadrao implements GeradorAleatorio {

    @Override
    public int proximoInt(int minInclusivo, int maxInclusivo) {
        if (minInclusivo >= maxInclusivo) {
            return minInclusivo;
        }
        return ThreadLocalRandom.current().nextInt(minInclusivo, maxInclusivo + 1);
    }

    @Override
    public double proximaProbabilidade() {
        return ThreadLocalRandom.current().nextDouble(0.0, 100.0);
    }

    @Override
    public boolean testarChance(double probabilidadePercentual) {
        double chanceLimitada = Math.max(0.0, Math.min(95.0, probabilidadePercentual));
        return proximaProbabilidade() < chanceLimitada;
    }

    @Override
    public boolean testarChanceComSorte(double chanceBasePercentual, int sorte) {
        double bonusSorte = Math.max(0, sorte) * 0.35;
        return testarChance(Math.min(90.0, chanceBasePercentual + bonusSorte));
    }
}
