package br.com.rpg;

import br.com.rpg.model.Arqueiro;
import br.com.rpg.model.Atributos;
import br.com.rpg.model.Guerreiro;
import br.com.rpg.model.InimigoComum;
import br.com.rpg.model.Mago;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoAtaque;
import br.com.rpg.rng.GeradorAleatorio;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MotorCombateTest {

    private static class RngDeterministico implements GeradorAleatorio {
        private final boolean sempreAcertaChance;

        RngDeterministico(boolean sempreAcertaChance) {
            this.sempreAcertaChance = sempreAcertaChance;
        }

        @Override
        public int proximoInt(int minInclusivo, int maxInclusivo) {
            return minInclusivo;
        }

        @Override
        public double proximaProbabilidade() {
            return sempreAcertaChance ? 1.0 : 99.0;
        }

        @Override
        public boolean testarChance(double probabilidadePercentual) {
            return sempreAcertaChance && probabilidadePercentual > 0;
        }

        @Override
        public boolean testarChanceComSorte(double chanceBasePercentual, int sorte) {
            return sempreAcertaChance;
        }
    }

    @Test
    void devePermitirEsquivaQuandoTesteDeEsquivaPassa() {
        Personagem arqueiro = new Arqueiro("Vaelin");
        InimigoComum goblin = new InimigoComum("Goblin", "Assassino", new Atributos(60, 15, 4, 5, 10.0, 5.0), 30, 15);
        GeradorAleatorio rngEsquivaGarantida = new RngDeterministico(true);

        ResultadoAtaque resultado = goblin.atacar(arqueiro, rngEsquivaGarantida);

        assertTrue(resultado.esquivou(), "O arqueiro deveria esquivar quando o teste de esquiva retorna true.");
        assertEquals(0, resultado.danoCausado(), "Dano deve ser 0 após uma esquiva bem-sucedida.");
    }

    @Test
    void deveAplicarDanoCriticoQuandoEsquivaFalhaECriticoPassa() {
        Personagem mago = new Mago("Arthur");
        InimigoComum alvoSemEsquiva = new InimigoComum("Golem", "Construto", new Atributos(200, 10, 5, 1, 0.0, 0.0), 50, 20) {
            @Override
            public boolean tentarEsquivar(GeradorAleatorio rng) {
                return false;
            }
        };

        GeradorAleatorio rngCritico = new RngDeterministico(true);
        ResultadoAtaque resultado = mago.atacar(alvoSemEsquiva, rngCritico);

        assertFalse(resultado.esquivou());
        assertTrue(resultado.critico(), "Com sorte e rolagem favorável, o golpe deve ser crítico.");
        assertTrue(resultado.danoCausado() > 20);
    }

    @Test
    void todasSubclassesDevemRespeitarContratoLiskov() {
        List<Personagem> herois = List.of(
                new Guerreiro("Kael"),
                new Mago("Merlin"),
                new Arqueiro("Lyra")
        );
        GeradorAleatorio rngSemEsquiva = new RngDeterministico(false);

        for (Personagem heroi : herois) {
            InimigoComum dummy = new InimigoComum("Boneco de Treino", "Alvo", new Atributos(100, 5, 2, 1, 0.0, 0.0), 10, 5);
            ResultadoAtaque res = heroi.usarHabilidadeEspecial(dummy, rngSemEsquiva);
            assertNotNull(res);
            assertTrue(res.danoCausado() > 0, "Toda habilidade especial deve causar dano válido.");
        }
    }
}
