package br.com.rpg.item;

import br.com.rpg.model.Raridade;

public abstract class Item {
    private final String nome;
    private final String descricao;
    private final Raridade raridade;
    private final int valorOuro;

    protected Item(String nome, String descricao, Raridade raridade, int valorOuro) {
        this.nome = nome;
        this.descricao = descricao;
        this.raridade = raridade;
        this.valorOuro = Math.max(1, valorOuro);
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Raridade getRaridade() {
        return raridade;
    }

    public int getValorOuro() {
        return valorOuro;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s", raridade.getRotulo(), nome, descricao);
    }
}
