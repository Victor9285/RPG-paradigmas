package br.com.rpg;

import br.com.rpg.event.EventoBauMisterioso;
import br.com.rpg.event.EventoEmboscada;
import br.com.rpg.model.Atributos;
import br.com.rpg.model.Guerreiro;
import br.com.rpg.rng.GeradorAleatorio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventoDanoTest {

    private static final GeradorAleatorio RNG_SEM_SORTE = new GeradorAleatorio() {
        @Override
        public int proximoInt(int minInclusivo, int maxInclusivo) {
            return minInclusivo;
        }

        @Override
        public double proximaProbabilidade() {
            return 100.0;
        }

        @Override
        public boolean testarChance(double probabilidadePercentual) {
            return false;
        }

        @Override
        public boolean testarChanceComSorte(double chanceBasePercentual, int sorte) {
            return false;
        }
    };

    @Test
    void armadilhaDoBauNaoPodeMatarPersonagemComPoucaVida() {
        Guerreiro jogador = new Guerreiro("Teste");
        jogador.getAtributos().setVidaAtual(5);

        new EventoBauMisterioso().executar(jogador, RNG_SEM_SORTE);

        assertEquals(1, jogador.getAtributos().getVidaAtual());
    }

    @Test
    void emboscadaNaoPodeMatarPersonagemComPoucaVida() {
        Guerreiro jogador = new Guerreiro("Teste");
        jogador.getAtributos().setVidaAtual(5);

        new EventoEmboscada().executar(jogador, RNG_SEM_SORTE);

        assertEquals(1, jogador.getAtributos().getVidaAtual());
    }

    @Test
    void danoNaoFatalPreservaPersonagemJaComUmPontoDeVida() {
        Atributos atributos = new Atributos(100, 10, 5, 5, 0.0, 0.0);
        atributos.setVidaAtual(1);

        atributos.sofrerDanoNaoFatal(20);

        assertEquals(1, atributos.getVidaAtual());
    }
}
