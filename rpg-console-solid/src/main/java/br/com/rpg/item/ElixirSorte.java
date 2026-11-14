package br.com.rpg.item;

import br.com.rpg.interfaces.ItemUsavel;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;

public class ElixirSorte extends Item implements ItemUsavel {
    private final int bonusSortePermanente;

    public ElixirSorte(String nome, Raridade raridade, int bonusSortePermanente, int valorOuro) {
        super(nome, "Aumenta permanentemente +" + bonusSortePermanente + " de Sorte (Luck).", raridade, valorOuro);
        this.bonusSortePermanente = bonusSortePermanente;
    }

    @Override
    public String usar(Personagem alvo) {
        alvo.getAtributos().aumentarSorte(bonusSortePermanente);
        return String.format("%s consumiu %s! Sorte permanente aumentada em +%d (Total: %d).",
                alvo.getNome(), getNome(), bonusSortePermanente, alvo.getSorteTotal());
    }

    public int getBonusSortePermanente() {
        return bonusSortePermanente;
    }
}
