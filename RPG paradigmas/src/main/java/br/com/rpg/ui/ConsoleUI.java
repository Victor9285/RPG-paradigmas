package br.com.rpg.ui;

import br.com.rpg.item.Equipamento;
import br.com.rpg.model.Inimigo;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.SlotEquipamento;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Map;
import java.util.Scanner;

public class ConsoleUI implements InterfaceUsuario {
    private final Scanner scanner;

    public ConsoleUI() {
        try {
            AnsiConsole.systemInstall();
        } catch (Throwable ignored) {
        }
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void exibirTitulo(String titulo) {
        String borda = "============================================================";
        System.out.println(Ansi.ansi().fgBrightCyan().a("\n" + borda).reset());
        System.out.println(Ansi.ansi().fgBrightCyan().bold().a("  " + titulo).reset());
        System.out.println(Ansi.ansi().fgBrightCyan().a(borda).reset());
    }

    @Override
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public void exibirSucesso(String mensagem) {
        System.out.println(Ansi.ansi().fgBrightGreen().a("[+] " + mensagem).reset());
    }

    @Override
    public void exibirAlerta(String mensagem) {
        System.out.println(Ansi.ansi().fgBrightYellow().a("[!] " + mensagem).reset());
    }

    @Override
    public void exibirPerigo(String mensagem) {
        System.out.println(Ansi.ansi().fgBrightRed().a("[x] " + mensagem).reset());
    }

    @Override
    public void exibirPainelCombate(Personagem jogador, Inimigo inimigo, int turno) {
        System.out.println(Ansi.ansi().fgBrightMagenta().a("\n-------------------- TURNO " + turno + " --------------------").reset());
        System.out.printf("HERÓI: %s (%s Nv.%d) | PV: %d/%d | Esquiva: %.1f%% | Sorte: %d%n",
                jogador.getNome(),
                jogador.getNomeClasse(),
                jogador.getAtributos().getNivel(),
                jogador.getAtributos().getVidaAtual(),
                jogador.getAtributos().getVidaMaxima(),
                jogador.getChanceEsquivaTotal(),
                jogador.getSorteTotal());

        String tagChefe = inimigo.isChefe() ? " [CHEFE]" : "";
        System.out.printf("INIMIGO: %s%s (%s) | PV: %d/%d | Esquiva: %.1f%%%n",
                inimigo.getNome(),
                tagChefe,
                inimigo.getNomeClasse(),
                inimigo.getAtributos().getVidaAtual(),
                inimigo.getAtributos().getVidaMaxima(),
                inimigo.getChanceEsquivaTotal());
        System.out.println("---------------------------------------------------");
    }

    @Override
    public void exibirFichaCompleta(Personagem jogador) {
        exibirTitulo("FICHA DO PERSONAGEM: " + jogador.getNome().toUpperCase());
        var attr = jogador.getAtributos();
        System.out.printf("Classe: %s | Nível: %d | XP: %d/%d | Ouro: %d G%n",
                jogador.getNomeClasse(), attr.getNivel(), attr.getExperiencia(), attr.getExperienciaProximoNivel(), attr.getOuro());
        System.out.printf("Vida: %d/%d%n", attr.getVidaAtual(), attr.getVidaMaxima());
        System.out.printf("Ataque Total: %d (Base %d + Equip %d)%n",
                jogador.getAtaqueTotal(), attr.getAtaque(), jogador.getInventario().getBonusAtaqueTotal());
        System.out.printf("Defesa Total: %d (Base %d + Equip %d)%n",
                jogador.getDefesaTotal(), attr.getDefesa(), jogador.getInventario().getBonusDefesaTotal());
        System.out.printf("Sorte (Luck): %d (Base %d + Equip %d) -> Crítico Efetivo: %.1f%%%n",
                jogador.getSorteTotal(), attr.getSorte(), jogador.getInventario().getBonusSorteTotal(), jogador.getChanceCriticoTotal());
        System.out.printf("Esquiva Total: %.1f%% (Base %.1f%% + Equip %.1f%% + Bônus Sorte)%n",
                jogador.getChanceEsquivaTotal(), attr.getEsquivaBase(), jogador.getInventario().getBonusEsquivaTotal());

        System.out.println("\nEquipamentos Ativos:");
        Map<SlotEquipamento, Equipamento> eqs = jogador.getInventario().getEquipados();
        for (SlotEquipamento slot : SlotEquipamento.values()) {
            Equipamento eq = eqs.get(slot);
            System.out.printf("  - %-18s: %s%n", slot.getDescricao(), eq != null ? eq.toString() : "(Vazio)");
        }
    }

    @Override
    public int lerOpcao(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt + " ");
            String linha = scanner.nextLine().trim();
            try {
                int valor = Integer.parseInt(linha);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException ignored) {
            }
            exibirAlerta("Entrada inválida. Digite um número entre " + min + " e " + max + ".");
        }
    }

    @Override
    public String lerTexto(String prompt) {
        while (true) {
            System.out.print(prompt + " ");
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            exibirAlerta("O texto não pode ficar vazio.");
        }
    }
}
