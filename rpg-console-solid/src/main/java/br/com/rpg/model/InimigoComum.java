package br.com.rpg.model;

import br.com.rpg.rng.GeradorAleatorio;

public class InimigoComum extends Inimigo {
    private final String arquetipo;

    public InimigoComum(String nome, String arquetipo, Atributos atributos, int recompensaXp, int recompensaOuro) {
        super(nome, atributos, recompensaXp, recompensaOuro, false);
        this.arquetipo = arquetipo;
    }

    @Override
    public String getNomeClasse() {
        return arquetipo;
    }

    @Override
    public String getDescricaoClasse() {
        return "Criatura hostil encontrada nas masmorras.";
    }

    @Override
    public String getNomeHabilidade() {
        return "Investida Selvagem";
    }

    @Override
    public ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng) {
        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(nome, alvo.getNome(), 0, true, false,
                    alvo.getNome() + " desviou no último segundo da Investida Selvagem de " + nome + "!");
        }
        int danoBruto = (int) Math.round(getAtaqueTotal() * 1.40);
        int danoEfetivo = alvo.receberDanoFinal(danoBruto);
        return new ResultadoAtaque(nome, alvo.getNome(), danoEfetivo, false, false,
                String.format("%s executou Investida Selvagem em %s causando %d de dano!", nome, alvo.getNome(), danoEfetivo));
    }
}
