package br.com.rpg.model;

import br.com.rpg.rng.GeradorAleatorio;

public class Arqueiro extends Personagem {

    public Arqueiro(String nome) {
        super(nome, new Atributos(115, 20, 8, 22, 22.0, 20.0));
    }

    @Override
    public String getNomeClasse() {
        return "Arqueiro";
    }

    @Override
    public String getDescricaoClasse() {
        return "Caçador ágil com altíssima Esquiva e Luck, especialista em críticos e loot raro.";
    }

    @Override
    public String getNomeHabilidade() {
        return "Chuva de Flechas & Passo Sombrio";
    }

    @Override
    public ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng) {
        aplicarBonusEsquivaTemporario(22.0);

        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(nome, alvo.getNome(), 0, true, false,
                    alvo.getNome() + " bloqueou a salva, mas " + nome + " ativou Passo Sombrio (+22% Esquiva)!");
        }

        int danoBruto = (int) Math.round(getAtaqueTotal() * 1.55);
        boolean critico = rng.testarChance(getChanceCriticoTotal() + 15.0);
        if (critico) {
            danoBruto = (int) Math.round(danoBruto * 1.80);
        }
        int danoEfetivo = alvo.receberDanoFinal(danoBruto);
        String texto = String.format("%s disparou %s%s causando %d de dano e ganhando +22%% de Esquiva!",
                nome, getNomeHabilidade(), critico ? " [TIRO LETAL]" : "", danoEfetivo);
        return new ResultadoAtaque(nome, alvo.getNome(), danoEfetivo, false, critico, texto);
    }
}
