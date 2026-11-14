package br.com.rpg;

import br.com.rpg.event.EventoBauMisterioso;
import br.com.rpg.event.EventoEmboscada;
import br.com.rpg.event.EventoFonteMistica;
import br.com.rpg.event.EventoSantuarioAntigo;
import br.com.rpg.event.GerenciadorEventos;
import br.com.rpg.rng.GeradorAleatorio;
import br.com.rpg.rng.GeradorAleatorioPadrao;
import br.com.rpg.service.GeradorLoot;
import br.com.rpg.service.JogoService;
import br.com.rpg.service.MotorCombate;
import br.com.rpg.service.PersistenciaService;
import br.com.rpg.ui.ConsoleUI;
import br.com.rpg.ui.InterfaceUsuario;

import java.nio.file.Path;
import java.util.List;

/**
 * Ponto de entrada da aplicação (Composition Root), injetando todas as dependências concretas
 * nas abstrações correspondentes (Princípio da Inversão de Dependência - DIP).
 */
public class Main {
    public static void main(String[] args) {
        InterfaceUsuario ui = new ConsoleUI();
        GeradorAleatorio rng = new GeradorAleatorioPadrao();
        GeradorLoot geradorLoot = new GeradorLoot(rng);
        MotorCombate motorCombate = new MotorCombate(rng, ui);

        GerenciadorEventos gerenciadorEventos = new GerenciadorEventos(
                List.of(
                        new EventoBauMisterioso(),
                        new EventoSantuarioAntigo(),
                        new EventoEmboscada(),
                        new EventoFonteMistica()
                ),
                rng
        );

        PersistenciaService persistenciaService = new PersistenciaService(Path.of("savegame.json"));

        JogoService jogo = new JogoService(
                ui,
                rng,
                geradorLoot,
                motorCombate,
                gerenciadorEventos,
                persistenciaService
        );

        jogo.iniciar();
    }
}
