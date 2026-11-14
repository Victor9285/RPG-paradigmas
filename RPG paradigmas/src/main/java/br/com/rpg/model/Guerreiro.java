package br.com.rpg.model;

import br.com.rpg.rng.GeradorAleatorio;

public class Guerreiro extends Personagem {

    public Guerreiro(String nome) {
        super(nome, new Atributos(145, 18, 13, 8, 8.0, 10.0));
    }

    @Override
    public String getNomeClasse() {
        return "Guerreiro";
    }

    @Override
    public String getDescricaoClasse() {
        return "Vanguarda blindada com alta resistência e golpes pesados que ignoram parte da defesa inimiga.";
    }

    @Override
    public String getNomeHabilidade() {
        return "Golpe Sísmico";
    }

    @Override
    public ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng) {
        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(nome, alvo.getNome(), 0, true, false,
                    alvo.getNome() + " saltou para trás e esquivou do Golpe Sísmico!");
        }
        int danoBruto = (int) Math.round(getAtaqueTotal() * 1.75);
        boolean critico = rng.testarChance(getChanceCriticoTotal());
        if (critico) {
            danoBruto = (int) Math.round(danoBruto * 1.5);
        }
        int defesaReduzida = alvo.getDefesaTotal() / 2;
        int danoEfetivo = alvo.getAtributos().receberDanoBruto(danoBruto, defesaReduzida);
        aplicarBonusEsquivaTemporario(10.0);

        String texto = String.format("%s desferiu %s%s em %s causando %d de dano perfurante (+10%% postura defensiva)!",
                nome, getNomeHabilidade(), critico ? " [CRÍTICO]" : "", alvo.getNome(), danoEfetivo);
        return new ResultadoAtaque(nome, alvo.getNome(), danoEfetivo, false, critico, texto);
    }
}
