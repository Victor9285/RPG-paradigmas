package br.com.rpg.event;

import br.com.rpg.interfaces.EventoAleatorio;
import br.com.rpg.item.ElixirSorte;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

public class EventoBauMisterioso implements EventoAleatorio {

    @Override
    public String getTitulo() {
        return "Baú Rúnico Esquecido";
    }

    @Override
    public int getPesoBase() {
        return 30;
    }

    @Override
    public ResultadoEvento executar(Personagem jogador, GeradorAleatorio rng) {
        boolean teveSorte = rng.testarChanceComSorte(45.0, jogador.getSorteTotal());
        if (teveSorte) {
            int ouroEncontrado = rng.proximoInt(35, 75) + jogador.getSorteTotal();
            jogador.getAtributos().adicionarOuro(ouroEncontrado);
            jogador.getInventario().adicionarItem(new ElixirSorte("Elixir da Fortuna", Raridade.RARO, 3, 60));
            return new ResultadoEvento(
                    getTitulo(),
                    "Sua Sorte brilhou! O mecanismo antigo abriu sem disparar armadilhas, revelando ouro e um Elixir da Fortuna!",
                    true, 0, ouroEncontrado, 20, 0
            );
        } else {
            int danoArmadilha = rng.proximoInt(8, 16);
            jogador.getAtributos().sofrerDanoNaoFatal(danoArmadilha);
            return new ResultadoEvento(
                    getTitulo(),
                    "Falta de sorte! Uma agulha envenenada saltou da fechadura antes que você pudesse recuar.",
                    false, -danoArmadilha, 0, 5, 0
            );
        }
    }
}
