package br.com.rpg.model;

public enum Raridade {
    COMUM("Comum", 1.0),
    RARO("Raro", 1.35),
    EPICO("Épico", 1.75),
    LENDARIO("Lendário", 2.30);

    private final String rotulo;
    private final double multiplicadorAtributos;

    Raridade(String rotulo, double multiplicadorAtributos) {
        this.rotulo = rotulo;
        this.multiplicadorAtributos = multiplicadorAtributos;
    }

    public String getRotulo() {
        return rotulo;
    }

    public double getMultiplicadorAtributos() {
        return multiplicadorAtributos;
    }
}
