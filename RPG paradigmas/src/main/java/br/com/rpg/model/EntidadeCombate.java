package br.com.rpg.model;

import br.com.rpg.interfaces.Atacante;
import br.com.rpg.interfaces.Esquivavel;
import br.com.rpg.interfaces.HabilidadeEspecial;
import br.com.rpg.rng.GeradorAleatorio;

/**
 * Classe base abstrata para todas as entidades combatentes (Heróis, Inimigos Comuns e Chefes).
 * Garante o Princípio da Substituição de Liskov (LSP): qualquer subclasse pode substituir EntidadeCombate
 * no MotorCombate mantendo invariantes de dano, esquiva, crítico e ciclo de vida.
 */
public abstract class EntidadeCombate implements Atacante, Esquivavel, HabilidadeEspecial {
    protected final String nome;
    protected final Atributos atributos;
    protected double bonusEsquivaTemporario;

    protected EntidadeCombate(String nome, Atributos atributos) {
        this.nome = nome;
        this.atributos = atributos;
        this.bonusEsquivaTemporario = 0.0;
    }

    public int getAtaqueTotal() {
        return atributos.getAtaque();
    }

    public int getDefesaTotal() {
        return atributos.getDefesa();
    }

    public int getSorteTotal() {
        return atributos.getSorte();
    }

    @Override
    public double getChanceEsquivaTotal() {
        double total = atributos.getEsquivaBase() + bonusEsquivaTemporario + (getSorteTotal() * 0.25);
        return Math.min(65.0, Math.max(0.0, total));
    }

    public double getChanceCriticoTotal() {
        double total = atributos.getChanceCriticoBase() + (getSorteTotal() * 0.40);
        return Math.min(75.0, Math.max(0.0, total));
    }

    @Override
    public boolean tentarEsquivar(GeradorAleatorio rng) {
        boolean esquivou = rng.testarChance(getChanceEsquivaTotal());
        this.bonusEsquivaTemporario = 0.0;
        return esquivou;
    }

    @Override
    public ResultadoAtaque atacar(EntidadeCombate alvo, GeradorAleatorio rng) {
        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(
                    this.nome,
                    alvo.getNome(),
                    0,
                    true,
                    false,
                    alvo.getNome() + " leu o movimento e ESQUIVOU agilmente do ataque de " + this.nome + "!"
            );
        }

        int variacao = rng.proximoInt(-2, 3);
        int danoBase = Math.max(1, getAtaqueTotal() + variacao);
        boolean critico = rng.testarChance(getChanceCriticoTotal());
        if (critico) {
            danoBase = (int) Math.round(danoBase * 1.75);
        }

        int danoEfetivo = alvo.receberDanoFinal(danoBase);
        String msg = critico
                ? String.format("GOLPE CRÍTICO DE SORTE! %s atingiu %s causando %d de dano!", this.nome, alvo.getNome(), danoEfetivo)
                : String.format("%s atacou %s causando %d de dano.", this.nome, alvo.getNome(), danoEfetivo);

        return new ResultadoAtaque(this.nome, alvo.getNome(), danoEfetivo, false, critico, msg);
    }

    public int receberDanoFinal(int danoBruto) {
        return atributos.receberDanoBruto(danoBruto, getDefesaTotal());
    }

    public void aplicarBonusEsquivaTemporario(double bonus) {
        this.bonusEsquivaTemporario = Math.max(0.0, bonus);
    }

    public boolean estaVivo() {
        return atributos.estaVivo();
    }

    public String getNome() {
        return nome;
    }

    public Atributos getAtributos() {
        return atributos;
    }

    public abstract String getNomeClasse();

    public abstract String getDescricaoClasse();
}
