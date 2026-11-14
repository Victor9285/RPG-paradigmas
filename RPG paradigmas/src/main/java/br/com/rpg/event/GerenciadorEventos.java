package br.com.rpg.event;

import br.com.rpg.interfaces.EventoAleatorio;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

import java.util.List;

/**
 * Orquestra eventos aleatórios durante a exploração (OCP + DIP).
 */
public class GerenciadorEventos {
    private final List<EventoAleatorio> eventosDisponiveis;
    private final GeradorAleatorio rng;

    public GerenciadorEventos(List<EventoAleatorio> eventosDisponiveis, GeradorAleatorio rng) {
        this.eventosDisponiveis = List.copyOf(eventosDisponiveis);
        this.rng = rng;
    }

    public ResultadoEvento sortearEExecutar(Personagem jogador) {
        int pesoTotal = eventosDisponiveis.stream().mapToInt(EventoAleatorio::getPesoBase).sum();
        int sorteio = rng.proximoInt(1, Math.max(1, pesoTotal));
        int acumulado = 0;

        for (EventoAleatorio evento : eventosDisponiveis) {
            acumulado += evento.getPesoBase();
            if (sorteio <= acumulado) {
                return evento.executar(jogador, rng);
            }
        }
        return eventosDisponiveis.get(0).executar(jogador, rng);
    }
}
