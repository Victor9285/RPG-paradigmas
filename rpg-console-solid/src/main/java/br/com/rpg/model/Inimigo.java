package br.com.rpg.model;

public abstract class Inimigo extends EntidadeCombate {
    private final int recompensaXp;
    private final int recompensaOuro;
    private final boolean chefe;

    protected Inimigo(String nome, Atributos atributos, int recompensaXp, int recompensaOuro, boolean chefe) {
        super(nome, atributos);
        this.recompensaXp = recompensaXp;
        this.recompensaOuro = recompensaOuro;
        this.chefe = chefe;
    }

    public int getRecompensaXp() {
        return recompensaXp;
    }

    public int getRecompensaOuro() {
        return recompensaOuro;
    }

    public boolean isChefe() {
        return chefe;
    }
}
