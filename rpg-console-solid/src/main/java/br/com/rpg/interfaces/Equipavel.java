package br.com.rpg.interfaces;

import br.com.rpg.model.Personagem;
import br.com.rpg.model.SlotEquipamento;

/**
 * Interface Segregada (ISP): Apenas equipamentos (armas, armaduras, amuletos) implementam esta interface,
 * evitando obrigar poções a terem métodos de equipar/desequipar.
 */
public interface Equipavel {
    SlotEquipamento getSlot();
    void aoEquipar(Personagem alvo);
    void aoDesequipar(Personagem alvo);
}
