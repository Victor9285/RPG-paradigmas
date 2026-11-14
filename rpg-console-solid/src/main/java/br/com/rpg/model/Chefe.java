package br.com.rpg.model;

import br.com.rpg.rng.GeradorAleatorio;

public class Chefe extends Inimigo {
    private boolean emFuria = false;

    public Chefe(String nome, Atributos atributos, int recompensaXp, int recompensaOuro) {
        super(nome, atributos, recompensaXp, recompensaOuro, true);
    }

    @Override
    public int getAtaqueTotal() {
        verificarFuria();
        return emFuria ? (int) Math.round(super.getAtaqueTotal() * 1.25) : super.getAtaqueTotal();
    }

    private void verificarFuria() {
        if (!emFuria && atributos.getVidaAtual() <= atributos.getVidaMaxima() * 0.40) {
            emFuria = true;
        }
    }

    @Override
    public String getNomeClasse() {
        return "Chefe Abissal";
    }

    @Override
    public String getDescricaoClasse() {
        return "Guardião de elite que entra em estado de Fúria ao ficar com menos de 40% de vida.";
    }

    @Override
    public String getNomeHabilidade() {
        return "Cataclismo Sombrio";
    }

    @Override
    public ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng) {
        verificarFuria();
        if (alvo.tentarEsquivar(rng)) {
            return new ResultadoAtaque(nome, alvo.getNome(), 0, true, false,
                    alvo.getNome() + " realizou uma esquiva milagrosa contra o Cataclismo Sombrio!");
        }
        int danoBruto = (int) Math.round(getAtaqueTotal() * 1.70);
        boolean critico = rng.testarChance(getChanceCriticoTotal());
        if (critico) {
            danoBruto = (int) Math.round(danoBruto * 1.45);
        }
        int danoEfetivo = alvo.receberDanoFinal(danoBruto);
        String estado = emFuria ? " [MODO FÚRIA]" : "";
        return new ResultadoAtaque(nome, alvo.getNome(), danoEfetivo, false, critico,
                String.format("CHEFE %s%s lançou Cataclismo Sombrio causando %d de dano devastador em %s!",
                        nome, estado, danoEfetivo, alvo.getNome()));
    }
}
