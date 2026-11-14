package br.com.rpg.item;

import br.com.rpg.interfaces.ItemUsavel;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;

public class PocaoCura extends Item implements ItemUsavel {
    private final int pontosCura;

    public PocaoCura(String nome, Raridade raridade, int pontosCura, int valorOuro) {
        super(nome, "Restaura até " + pontosCura + " PV imediatamente.", raridade, valorOuro);
        this.pontosCura = pontosCura;
    }

    @Override
    public String usar(Personagem alvo) {
        int recuperado = alvo.getAtributos().curar(pontosCura);
        return String.format("%s bebeu %s e recuperou %d PV!", alvo.getNome(), getNome(), recuperado);
    }

    public int getPontosCura() {
        return pontosCura;
    }
}
