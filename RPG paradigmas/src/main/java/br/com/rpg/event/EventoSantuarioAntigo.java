package br.com.rpg.event;

import br.com.rpg.interfaces.EventoAleatorio;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

public class EventoSantuarioAntigo implements EventoAleatorio {

    @Override
    public String getTitulo() {
        return "Santuário da Deusa da Fortuna";
    }

    @Override
    public int getPesoBase() {
        return 25;
    }

    @Override
    public ResultadoEvento executar(Personagem jogador, GeradorAleatorio rng) {
        boolean bencaoMaior = rng.testarChanceComSorte(40.0, jogador.getSorteTotal());
        int bonusSorte = bencaoMaior ? 4 : 2;
        int cura = jogador.getAtributos().curar(bencaoMaior ? 45 : 25);
        jogador.getAtributos().aumentarSorte(bonusSorte);
        jogador.getAtributos().ganharExperiencia(30);

        return new ResultadoEvento(
                getTitulo(),
                bencaoMaior
                        ? "Uma luz dourada envolve você! Sua Sorte recebeu uma bênção rara e suas feridas cicatrizaram."
                        : "Você medita diante do altar antigo e sente seus reflexos e intuição mais aguçados.",
                true, cura, 0, 30, bonusSorte
        );
    }
}
