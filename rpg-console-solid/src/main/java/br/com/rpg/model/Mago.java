package br.com.rpg.model;

import br.com.rpg.rng.GeradorAleatorio;

public class Mago extends Personagem {

    public Mago(String nome) {
        super(nome, new Atributos(100, 25, 6, 15, 12.0, 15.0));
    }

    @Override
    public String getNomeClasse() {
        return "Mago";
    }

    @Override
    public String getDescricaoClasse() {
        return "Conjurador arcano de alto dano explosivo; sua Sorte amplifica a instabilidade mágica.";
    }

    @Override
    public String getNomeHabilidade() {
        return "Explosão Arcana";
    }

    @Override
    public ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng) {
        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(nome, alvo.getNome(), 0, true, false,
                    alvo.getNome() + " dissipou a conjuração e esquivou da Explosão Arcana!");
        }
        int bonusArcanoSorte = (int) Math.round(getSorteTotal() * 0.75);
        int danoBruto = (int) Math.round(getAtaqueTotal() * 1.65) + bonusArcanoSorte;
        boolean critico = rng.testarChance(getChanceCriticoTotal() + 10.0);
        if (critico) {
            danoBruto = (int) Math.round(danoBruto * 1.65);
        }
        int danoEfetivo = alvo.receberDanoFinal(danoBruto);
        String texto = String.format("%s canalizou %s%s (Bônus Sorte +%d) causando %d de dano em %s!",
                nome, getNomeHabilidade(), critico ? " [SOBRECARGA CRÍTICA]" : "", bonusArcanoSorte, danoEfetivo, alvo.getNome());
        return new ResultadoAtaque(nome, alvo.getNome(), danoEfetivo, false, critico, texto);
    }
}
