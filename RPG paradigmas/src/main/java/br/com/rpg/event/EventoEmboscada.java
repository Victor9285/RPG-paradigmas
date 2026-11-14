package br.com.rpg.event;

import br.com.rpg.interfaces.EventoAleatorio;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

public class EventoEmboscada implements EventoAleatorio {

    @Override
    public String getTitulo() {
        return "Emboscada nas Sombras";
    }

    @Override
    public int getPesoBase() {
        return 25;
    }

    @Override
    public ResultadoEvento executar(Personagem jogador, GeradorAleatorio rng) {
        if (jogador.tentarEsquivar(rng)) {
            int xp = 35;
            jogador.getAtributos().ganharExperiencia(xp);
            return new ResultadoEvento(
                    getTitulo(),
                    "Flechas cortaram o ar, mas seus reflexos de Esquiva salvaram você ileso e ainda renderam experiência tática!",
                    true, 0, 0, xp, 0
            );
        }

        int dano = rng.proximoInt(10, 20);
        jogador.getAtributos().sofrerDanoNaoFatal(dano);
        return new ResultadoEvento(
                getTitulo(),
                "Saqueadores ocultos dispararam dardos das sombras antes de fugir!",
                false, -dano, 0, 10, 0
        );
    }
}
