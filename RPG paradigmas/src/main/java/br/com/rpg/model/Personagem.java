package br.com.rpg.model;

import br.com.rpg.item.Inventario;

public abstract class Personagem extends EntidadeCombate {
    private final Inventario inventario;

    protected Personagem(String nome, Atributos atributos) {
        super(nome, atributos);
        this.inventario = new Inventario();
    }

    @Override
    public int getAtaqueTotal() {
        return super.getAtaqueTotal() + inventario.getBonusAtaqueTotal();
    }

    @Override
    public int getDefesaTotal() {
        return super.getDefesaTotal() + inventario.getBonusDefesaTotal();
    }

    @Override
    public int getSorteTotal() {
        return super.getSorteTotal() + inventario.getBonusSorteTotal();
    }

    @Override
    public double getChanceEsquivaTotal() {
        double total = atributos.getEsquivaBase()
                + inventario.getBonusEsquivaTotal()
                + bonusEsquivaTemporario
                + (getSorteTotal() * 0.25);
        return Math.min(65.0, Math.max(0.0, total));
    }

    public int descansar() {
        int cura = (int) Math.round(atributos.getVidaMaxima() * 0.45);
        return atributos.curar(cura);
    }

    public Inventario getInventario() {
        return inventario;
    }
}
