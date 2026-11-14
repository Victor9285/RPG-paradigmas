package br.com.rpg;

import br.com.rpg.model.Guerreiro;
import br.com.rpg.rng.GeradorAleatorio;
import br.com.rpg.service.GeradorLoot;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GeradorLootTest {

    @Test
    void deveUsarChanceMinimaDeSessentaECincoPorCentoParaMonstrosComuns() {
        RngQueRegistraChance rng = new RngQueRegistraChance();
        GeradorLoot geradorLoot = new GeradorLoot(rng);

        assertNull(geradorLoot.gerarRecompensaVitoria(new Guerreiro("Teste"), false));

        assertEquals(65.0, rng.chanceBase);
    }

    private static class RngQueRegistraChance implements GeradorAleatorio {
        private double chanceBase;

        @Override
        public int proximoInt(int minInclusivo, int maxInclusivo) {
            return minInclusivo;
        }

        @Override
        public double proximaProbabilidade() {
            return 0.0;
        }

        @Override
        public boolean testarChance(double probabilidadePercentual) {
            return false;
        }

        @Override
        public boolean testarChanceComSorte(double chanceBasePercentual, int sorte) {
            chanceBase = chanceBasePercentual;
            return false;
        }
    }
}
