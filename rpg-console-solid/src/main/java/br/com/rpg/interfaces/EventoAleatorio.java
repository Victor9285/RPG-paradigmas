package br.com.rpg.interfaces;

import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.rng.GeradorAleatorio;

/**
 * Princípio Aberto/Fechado (OCP): Novos eventos aleatórios podem ser adicionados ao sistema
 * implementando esta interface sem alterar o código do GerenciadorEventos.
 */
public interface EventoAleatorio {
    String getTitulo();
    int getPesoBase();
    ResultadoEvento executar(Personagem jogador, GeradorAleatorio rng);
}
