package br.com.rpg.item;

import br.com.rpg.interfaces.Equipavel;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.SlotEquipamento;

public class Equipamento extends Item implements Equipavel {
    private final SlotEquipamento slot;
    private final int bonusAtaque;
    private final int bonusDefesa;
    private final int bonusSorte;
    private final double bonusEsquiva;

    public Equipamento(String nome, Raridade raridade, SlotEquipamento slot,
                       int bonusAtaque, int bonusDefesa, int bonusSorte, double bonusEsquiva, int valorOuro) {
        super(nome,
                String.format("%s | +ATK %d | +DEF %d | +SORTE %d | +ESQ %.1f%%",
                        slot.getDescricao(), bonusAtaque, bonusDefesa, bonusSorte, bonusEsquiva),
                raridade,
                valorOuro);
        this.slot = slot;
        this.bonusAtaque = bonusAtaque;
        this.bonusDefesa = bonusDefesa;
        this.bonusSorte = bonusSorte;
        this.bonusEsquiva = bonusEsquiva;
    }

    @Override
    public SlotEquipamento getSlot() {
        return slot;
    }

    @Override
    public void aoEquipar(Personagem alvo) {
    }

    @Override
    public void aoDesequipar(Personagem alvo) {
    }

    public int getBonusAtaque() {
        return bonusAtaque;
    }

    public int getBonusDefesa() {
        return bonusDefesa;
    }

    public int getBonusSorte() {
        return bonusSorte;
    }

    public double getBonusEsquiva() {
        return bonusEsquiva;
    }
}
