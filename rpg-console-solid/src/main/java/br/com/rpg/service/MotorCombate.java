package br.com.rpg.service;

import br.com.rpg.interfaces.ItemUsavel;
import br.com.rpg.item.Item;
import br.com.rpg.model.Inimigo;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.ResultadoAtaque;
import br.com.rpg.rng.GeradorAleatorio;
import br.com.rpg.ui.InterfaceUsuario;

import java.util.List;

/**
 * Responsabilidade Única (SRP): Orquestrar o fluxo de combate em turnos, habilidades especiais,
 * uso de itens em batalha, esquiva e fuga baseada em Sorte/Agilidade.
 */
public class MotorCombate {
    private final GeradorAleatorio rng;
    private final InterfaceUsuario ui;

    public MotorCombate(GeradorAleatorio rng, InterfaceUsuario ui) {
        this.rng = rng;
        this.ui = ui;
    }

    public ResultadoCombate iniciarBatalha(Personagem jogador, Inimigo inimigo) {
        ui.exibirTitulo("COMBATE INICIADO: " + jogador.getNome() + " VS " + inimigo.getNome());
        int turno = 1;
        int recargaHabilidade = 0;

        while (jogador.estaVivo() && inimigo.estaVivo()) {
            ui.exibirPainelCombate(jogador, inimigo, turno);

            String labelEspecial = recargaHabilidade == 0
                    ? jogador.getNomeHabilidade() + " (Pronta!)"
                    : jogador.getNomeHabilidade() + " (Recarga: " + recargaHabilidade + " turno(s))";

            ui.exibirMensagem("1. Ataque Básico");
            ui.exibirMensagem("2. Habilidade Especial: " + labelEspecial);
            ui.exibirMensagem("3. Usar Item da Mochila");
            ui.exibirMensagem("4. Tentar Fuga Tática (Esquiva + Sorte)");

            int escolha = ui.lerOpcao("Escolha sua ação:", 1, 4);
            boolean turnoConsumido = false;

            switch (escolha) {
                case 1 -> {
                    ResultadoAtaque res = jogador.atacar(inimigo, rng);
                    exibirResultadoAtaque(res);
                    turnoConsumido = true;
                }
                case 2 -> {
                    if (recargaHabilidade > 0) {
                        ui.exibirAlerta("Habilidade ainda em recarga! Escolha outra ação.");
                    } else {
                        ResultadoAtaque res = jogador.usarHabilidadeEspecial(inimigo, rng);
                        exibirResultadoAtaque(res);
                        recargaHabilidade = calcularRecargaHabilidade(jogador.getAtributos().getNivel());
                        turnoConsumido = true;
                    }
                }
                case 3 -> {
                    if (usarItemEmCombate(jogador)) {
                        turnoConsumido = true;
                    }
                }
                case 4 -> {
                    if (inimigo.isChefe()) {
                        ui.exibirPerigo("Não é possível fugir da arena de um Chefe Abissal!");
                    } else if (rng.testarChanceComSorte(jogador.getChanceEsquivaTotal() + 15.0, jogador.getSorteTotal())) {
                        ui.exibirSucesso("Você aproveitou uma brecha e escapou com sucesso!");
                        return ResultadoCombate.FUGIU;
                    } else {
                        ui.exibirPerigo("Tentativa de fuga falhou! O inimigo bloqueou sua saída.");
                        turnoConsumido = true;
                    }
                }
            }

            if (!turnoConsumido) {
                continue;
            }

            if (!inimigo.estaVivo()) {
                ui.exibirSucesso("VITÓRIA! " + inimigo.getNome() + " foi derrotado!");
                return ResultadoCombate.VITORIA;
            }

            ResultadoAtaque ataqueInimigo = rng.testarChance(35.0)
                    ? inimigo.usarHabilidadeEspecial(jogador, rng)
                    : inimigo.atacar(jogador, rng);
            exibirResultadoAtaque(ataqueInimigo);

            if (recargaHabilidade > 0) {
                recargaHabilidade--;
            }
            turno++;
        }

        return jogador.estaVivo() ? ResultadoCombate.VITORIA : ResultadoCombate.DERROTA;
    }

    private boolean usarItemEmCombate(Personagem jogador) {
        List<Item> usaveis = jogador.getInventario().getItensUsaveis();
        if (usaveis.isEmpty()) {
            ui.exibirAlerta("Você não possui poções ou elixires na mochila.");
            return false;
        }
        ui.exibirMensagem("Itens disponíveis:");
        for (int i = 0; i < usaveis.size(); i++) {
            ui.exibirMensagem((i + 1) + ". " + usaveis.get(i));
        }
        ui.exibirMensagem("0. Voltar");
        int idx = ui.lerOpcao("Selecione o item:", 0, usaveis.size());
        if (idx == 0) {
            return false;
        }
        Item escolhido = usaveis.get(idx - 1);
        if (escolhido instanceof ItemUsavel usavel) {
            String efeito = usavel.usar(jogador);
            jogador.getInventario().removerItem(escolhido);
            ui.exibirSucesso(efeito);
            return true;
        }
        return false;
    }

    private int calcularRecargaHabilidade(int nivel) {
        return Math.max(1, 3 - Math.max(0, nivel - 1) / 5);
    }

    private void exibirResultadoAtaque(ResultadoAtaque res) {
        if (res.esquivou()) {
            ui.exibirAlerta(res.descricao());
        } else if (res.critico()) {
            ui.exibirSucesso(res.descricao());
        } else {
            ui.exibirMensagem("-> " + res.descricao());
        }
    }

    public enum ResultadoCombate {
        VITORIA,
        DERROTA,
        FUGIU
    }
}
