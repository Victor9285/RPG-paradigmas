package br.com.rpg.event;

import br.com.rpg.interfaces.EventoAleatorio;
import br.com.rpg.item.PocaoCura;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

public class EventoFonteMistica implements EventoAleatorio {

    @Override
    public String getTitulo() {
        return "Fonte Cristalina Subterrânea";
    }

    @Override
    public int getPesoBase() {
        return 20;
    }

    @Override
    public ResultadoEvento executar(Personagem jogador, GeradorAleatorio rng) {
        int cura = jogador.getAtributos().curar(40);
        jogador.getInventario().adicionarItem(new PocaoCura("Poção de Vitalidade", Raridade.COMUM, 45, 25));
        return new ResultadoEvento(
                getTitulo(),
                "Você bebe da água cristalina, restaura suas energias e enche um frasco com Poção de Vitalidade.",
                true, cura, 0, 15, 0
        );
    }
}
