package br.com.rpg.interfaces;

import br.com.rpg.model.Personagem;

/**
 * Interface Segregada (ISP): Apenas itens consumíveis (poções, elixires) implementam esta interface.
 */
public interface ItemUsavel {
    String usar(Personagem alvo);
}
