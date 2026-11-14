package br.com.rpg.service;

import br.com.rpg.item.ElixirSorte;
import br.com.rpg.item.Equipamento;
import br.com.rpg.item.Item;
import br.com.rpg.item.PocaoCura;
import br.com.rpg.model.Atributos;
import br.com.rpg.model.Chefe;
import br.com.rpg.model.Inimigo;
import br.com.rpg.model.InimigoComum;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.SlotEquipamento;
import br.com.rpg.rng.GeradorAleatorio;

/**
 * Responsabilidade Única (SRP): Geração procedural de inimigos escalonados pelo nível do jogador
 * e sorteio de recompensas/equipamentos ponderados pelo atributo Sorte (Luck).
 */
public class GeradorLoot {
    private static final double CHANCE_MINIMA_DROP_MONSTRO = 65.0;

    private final GeradorAleatorio rng;

    public GeradorLoot(GeradorAleatorio rng) {
        this.rng = rng;
    }

    public Inimigo gerarInimigoParaNivel(int nivelJogador, boolean chefe) {
        int escala = Math.max(1, nivelJogador);
        if (chefe) {
            String[] nomesChefes = {
                    "Ignis, o Cavaleiro Cinzento",
                    "Vespera, Arquiteta do Eclipse",
                    "Malakor, Soberano do Abismo"
            };
            String nomeChefe = nomesChefes[(escala - 1) % nomesChefes.length];
            Atributos attr = new Atributos(
                    135 + (escala * 28),
                    17 + (escala * 4),
                    10 + (escala * 2),
                    12 + escala,
                    10.0 + escala,
                    14.0
            );
            return new Chefe(nomeChefe, attr, 120 + (escala * 35), 90 + (escala * 25));
        }

        String[][] catalogo = {
                {"Goblin Espreitador", "Assassino"},
                {"Esqueleto Guardião", "Morto-Vivo"},
                {"Lobo das Sombras", "Fera Ágil"},
                {"Cultista do Eclipse", "Ocultista"}
        };
        String[] escolhido = catalogo[rng.proximoInt(0, catalogo.length - 1)];
        Atributos attr = new Atributos(
                65 + (escala * 18),
                12 + (escala * 3),
                5 + escala,
                6 + escala,
                8.0 + (escala * 0.8),
                8.0
        );
        return new InimigoComum(escolhido[0], escolhido[1], attr, 45 + (escala * 15), 25 + (escala * 10));
    }

    public Raridade sortearRaridade(int sorteJogador, boolean garantidoRaroOuSuperior) {
        double bonus = sorteJogador * 0.45;
        double rolagem = rng.proximaProbabilidade() + bonus;

        if (rolagem >= 102.0) {
            return Raridade.LENDARIO;
        } else if (rolagem >= 82.0) {
            return Raridade.EPICO;
        } else if (rolagem >= 52.0 || garantidoRaroOuSuperior) {
            return Raridade.RARO;
        }
        return Raridade.COMUM;
    }

    public Item gerarRecompensaVitoria(Personagem jogador, boolean chefeDerrotado) {
        if (!chefeDerrotado
                && !rng.testarChanceComSorte(CHANCE_MINIMA_DROP_MONSTRO, jogador.getSorteTotal())) {
            return null;
        }

        Raridade raridade = sortearRaridade(jogador.getSorteTotal(), chefeDerrotado);
        int tipoDrop = rng.proximoInt(1, 100);

        if (tipoDrop <= 35) {
            int cura = (int) Math.round(45 * raridade.getMultiplicadorAtributos());
            return new PocaoCura("Poção de Cura " + raridade.getRotulo(), raridade, cura, (int) (25 * raridade.getMultiplicadorAtributos()));
        } else if (tipoDrop <= 55) {
            int bonusSorte = raridade == Raridade.LENDARIO ? 6 : (raridade == Raridade.EPICO ? 4 : 2);
            return new ElixirSorte("Elixir da Sorte " + raridade.getRotulo(), raridade, bonusSorte, 55);
        } else {
            return gerarEquipamentoAleatorio(jogador.getAtributos().getNivel(), raridade);
        }
    }

    public Equipamento gerarEquipamentoAleatorio(int nivel, Raridade raridade) {
        SlotEquipamento[] slots = SlotEquipamento.values();
        SlotEquipamento slot = slots[rng.proximoInt(0, slots.length - 1)];
        double mult = raridade.getMultiplicadorAtributos();
        int poderBase = Math.max(2, (int) Math.round((3 + nivel * 2) * mult));

        return switch (slot) {
            case ARMA -> new Equipamento(
                    "Lâmina Rúnica (" + raridade.getRotulo() + ")",
                    raridade,
                    SlotEquipamento.ARMA,
                    poderBase + 2,
                    0,
                    raridade == Raridade.LENDARIO ? 5 : 2,
                    1.5,
                    (int) (45 * mult)
            );
            case ARMADURA -> new Equipamento(
                    "Cota Sombraviva (" + raridade.getRotulo() + ")",
                    raridade,
                    SlotEquipamento.ARMADURA,
                    0,
                    poderBase + 1,
                    1,
                    2.5 * mult,
                    (int) (45 * mult)
            );
            case AMULETO -> new Equipamento(
                    "Talismã do Trevo Negro (" + raridade.getRotulo() + ")",
                    raridade,
                    SlotEquipamento.AMULETO,
                    1,
                    1,
                    (int) Math.round(5 * mult),
                    3.5 * mult,
                    (int) (55 * mult)
            );
        };
    }
}
