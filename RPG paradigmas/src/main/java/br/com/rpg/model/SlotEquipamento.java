package br.com.rpg.model;

public enum SlotEquipamento {
    ARMA("Arma"),
    ARMADURA("Armadura"),
    AMULETO("Amuleto da Sorte");

    private final String descricao;

    SlotEquipamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
