package br.com.rpg.service;

import br.com.rpg.event.GerenciadorEventos;
import br.com.rpg.interfaces.ItemUsavel;
import br.com.rpg.item.ElixirSorte;
import br.com.rpg.item.Equipamento;
import br.com.rpg.item.Item;
import br.com.rpg.item.PocaoCura;
import br.com.rpg.model.Arqueiro;
import br.com.rpg.model.Guerreiro;
import br.com.rpg.model.Inimigo;
import br.com.rpg.model.Mago;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.ResultadoEvento;
import br.com.rpg.model.SlotEquipamento;
import br.com.rpg.rng.GeradorAleatorio;
import br.com.rpg.ui.InterfaceUsuario;

import java.io.IOException;
import java.util.List;

public class JogoService {
    private final InterfaceUsuario ui;
    private final GeradorAleatorio rng;
    private final GeradorLoot geradorLoot;
    private final MotorCombate motorCombate;
    private final GerenciadorEventos gerenciadorEventos;
    private final PersistenciaService persistenciaService;

    private Personagem jogador;
    private int vitoriasTotais = 0;
    private int chefesDerrotados = 0;

    public JogoService(InterfaceUsuario ui,
                       GeradorAleatorio rng,
                       GeradorLoot geradorLoot,
                       MotorCombate motorCombate,
                       GerenciadorEventos gerenciadorEventos,
                       PersistenciaService persistenciaService) {
        this.ui = ui;
        this.rng = rng;
        this.geradorLoot = geradorLoot;
        this.motorCombate = motorCombate;
        this.gerenciadorEventos = gerenciadorEventos;
        this.persistenciaService = persistenciaService;
    }

    public void iniciar() {
        ui.exibirTitulo("CRÔNICAS DO ABISMO - RPG CONSOLE (JAVA 17 & SOLID)");

        if (persistenciaService.existeSave()) {
            ui.exibirMensagem("1. Novo Jogo");
            ui.exibirMensagem("2. Continuar Progresso Salvo (savegame.json)");
            int opcaoInicial = ui.lerOpcao("Escolha:", 1, 2);
            if (opcaoInicial == 2) {
                carregarProgresso();
            }
        }

        if (jogador == null) {
            criarNovoPersonagem();
        }

        executarLoopPrincipal();
    }

    private void criarNovoPersonagem() {
        ui.exibirTitulo("CRIAÇÃO DE PERSONAGEM");
        String nome = ui.lerTexto("Digite o nome do seu herói:");

        ui.exibirMensagem("\nEscolha sua classe:");
        ui.exibirMensagem("1. Guerreiro (Alta Vida/Defesa | Habilidade: Golpe Sísmico perfurante)");
        ui.exibirMensagem("2. Mago      (Alto Dano Arcano | Habilidade: Explosão escalada por Sorte)");
        ui.exibirMensagem("3. Arqueiro  (Alta Esquiva/Sorte | Habilidade: Chuva de Flechas + Passo Sombrio)");

        int classeEscolhida = ui.lerOpcao("Classe:", 1, 3);
        jogador = switch (classeEscolhida) {
            case 1 -> new Guerreiro(nome);
            case 2 -> new Mago(nome);
            default -> new Arqueiro(nome);
        };

        jogador.getInventario().adicionarItem(new PocaoCura("Poção de Cura Menor", Raridade.COMUM, 45, 20));
        jogador.getInventario().adicionarItem(new ElixirSorte("Amuleto Líquido da Sorte", Raridade.RARO, 3, 45));
        Equipamento armaInicial = new Equipamento("Adaga do Aprendiz", Raridade.COMUM, SlotEquipamento.ARMA, 3, 0, 2, 1.0, 25);
        jogador.getInventario().equiparItem(armaInicial, jogador);

        ui.exibirSucesso("Herói " + jogador.getNome() + " (" + jogador.getNomeClasse() + ") criado com sucesso!");
    }

    private void carregarProgresso() {
        try {
            var estado = persistenciaService.carregarJogo();
            if (estado != null) {
                this.jogador = estado.personagem();
                this.vitoriasTotais = estado.vitoriasTotais();
                this.chefesDerrotados = estado.chefesDerrotados();
                ui.exibirSucesso("Progresso carregado! Bem-vindo de volta, " + jogador.getNome() + " (Nível " + jogador.getAtributos().getNivel() + ").");
            }
        } catch (IOException e) {
            ui.exibirPerigo("Falha ao ler savegame.json: " + e.getMessage());
        }
    }

    private void executarLoopPrincipal() {
        boolean executando = true;
        while (executando && jogador.estaVivo()) {
            boolean proximoEhChefe = (vitoriasTotais > 0 && vitoriasTotais % 3 == 0);
            ui.exibirTitulo("ACAMPAMENTO DA MASMORRA | Nv." + jogador.getAtributos().getNivel()
                    + " | Vitórias: " + vitoriasTotais + " | Chefes: " + chefesDerrotados);
            ui.exibirMensagem(proximoEhChefe
                    ? "1. Enfrentar CHEFE ABISSAL (Desbloqueado!)"
                    : "1. Explorar Masmorra (Batalhas & Eventos Aleatórios)");
            ui.exibirMensagem("2. Ver Ficha Completa (Atributos, Esquiva & Sorte)");
            ui.exibirMensagem("3. Gerenciar Inventário & Equipamentos");
            ui.exibirMensagem("4. Mercador & Forja (Comprar Poções, Elixires e Baús)");
            ui.exibirMensagem("5. Descansar na Fogueira (Recupera 45% PV por 15 Ouro)");
            ui.exibirMensagem("6. Salvar Progresso (JSON)");
            ui.exibirMensagem("0. Sair do Jogo");

            int opcao = ui.lerOpcao("O que deseja fazer?", 0, 6);
            switch (opcao) {
                case 1 -> explorarMasmorra(proximoEhChefe);
                case 2 -> ui.exibirFichaCompleta(jogador);
                case 3 -> gerenciarInventario();
                case 4 -> abrirMercador();
                case 5 -> descansarNaFogueira();
                case 6 -> salvarProgresso();
                case 0 -> executando = false;
            }
        }

        if (!jogador.estaVivo()) {
            ui.exibirPerigo("\nFIM DE JOGO! " + jogador.getNome() + " tombou nas profundezas do Abismo.");
            ui.exibirMensagem("Estatísticas Finais -> Nível: " + jogador.getAtributos().getNivel()
                    + " | Vitórias: " + vitoriasTotais + " | Chefes Derrotados: " + chefesDerrotados);
        } else {
            ui.exibirMensagem("Até a próxima jornada!");
        }
    }

    private void explorarMasmorra(boolean forcarChefe) {
        if (!forcarChefe && vitoriasTotais >= 3 && rng.testarChance(35.0)) {
            ResultadoEvento evento = gerenciadorEventos.sortearEExecutar(jogador);
            ui.exibirTitulo("EVENTO ALEATÓRIO: " + evento.tituloEvento());
            if (evento.positivo()) {
                ui.exibirSucesso(evento.narrativa());
            } else {
                ui.exibirPerigo(evento.narrativa());
            }
            return;
        }

        Inimigo inimigo = geradorLoot.gerarInimigoParaNivel(jogador.getAtributos().getNivel(), forcarChefe);
        MotorCombate.ResultadoCombate resultado = motorCombate.iniciarBatalha(jogador, inimigo);

        if (resultado == MotorCombate.ResultadoCombate.VITORIA) {
            vitoriasTotais++;
            if (inimigo.isChefe()) {
                chefesDerrotados++;
            }
            jogador.getAtributos().adicionarOuro(inimigo.getRecompensaOuro());
            boolean subiuNivel = jogador.getAtributos().ganharExperiencia(inimigo.getRecompensaXp());
            ui.exibirSucesso(String.format("Recompensas: +%d XP | +%d Ouro!", inimigo.getRecompensaXp(), inimigo.getRecompensaOuro()));

            if (subiuNivel) {
                ui.exibirSucesso("LEVEL UP! Você alcançou o Nível " + jogador.getAtributos().getNivel() + " e seus atributos aumentaram!");
            }

            Item drop = geradorLoot.gerarRecompensaVitoria(jogador, inimigo.isChefe());
            if (drop != null) {
                jogador.getInventario().adicionarItem(drop);
                ui.exibirSucesso("LOOT ENCONTRADO (Influenciado pela Sorte): " + drop);
            }
        }
    }

    private void gerenciarInventario() {
        ui.exibirTitulo("INVENTÁRIO & EQUIPAMENTOS");
        List<Item> itens = jogador.getInventario().getItens();
        if (itens.isEmpty()) {
            ui.exibirAlerta("Sua mochila está vazia.");
            return;
        }
        for (int i = 0; i < itens.size(); i++) {
            ui.exibirMensagem((i + 1) + ". " + itens.get(i));
        }
        ui.exibirMensagem("0. Voltar");
        int escolha = ui.lerOpcao("Escolha um item para usar ou equipar:", 0, itens.size());
        if (escolha == 0) {
            return;
        }

        Item selecionado = itens.get(escolha - 1);
        if (selecionado instanceof Equipamento eq) {
            ui.exibirSucesso(jogador.getInventario().equiparItem(eq, jogador));
        } else if (selecionado instanceof ItemUsavel usavel) {
            ui.exibirSucesso(usavel.usar(jogador));
            jogador.getInventario().removerItem(selecionado);
        }
    }

    private void abrirMercador() {
        ui.exibirTitulo("MERCADOR ERRANTE (Ouro atual: " + jogador.getAtributos().getOuro() + " G)");
        ui.exibirMensagem("1. Poção de Cura Superior (+75 PV) - 35 Ouro");
        ui.exibirMensagem("2. Elixir da Sorte Concentrado (+4 Sorte permanente) - 65 Ouro");
        ui.exibirMensagem("3. Caixa Misteriosa (Item/Equipamento aleatório escalado pela sua Sorte) - 80 Ouro");
        ui.exibirMensagem("0. Voltar");

        int op = ui.lerOpcao("Comprar:", 0, 3);
        switch (op) {
            case 1 -> {
                if (jogador.getAtributos().gastarOuro(35)) {
                    jogador.getInventario().adicionarItem(new PocaoCura("Poção de Cura Superior", Raridade.RARO, 75, 35));
                    ui.exibirSucesso("Poção adquirida!");
                } else {
                    ui.exibirAlerta("Ouro insuficiente.");
                }
            }
            case 2 -> {
                if (jogador.getAtributos().gastarOuro(65)) {
                    jogador.getInventario().adicionarItem(new ElixirSorte("Elixir da Sorte Concentrado", Raridade.EPICO, 4, 65));
                    ui.exibirSucesso("Elixir da Sorte adquirido!");
                } else {
                    ui.exibirAlerta("Ouro insuficiente.");
                }
            }
            case 3 -> {
                if (jogador.getAtributos().gastarOuro(80)) {
                    Raridade raridade = geradorLoot.sortearRaridade(jogador.getSorteTotal(), false);
                    Equipamento eq = geradorLoot.gerarEquipamentoAleatorio(jogador.getAtributos().getNivel(), raridade);
                    jogador.getInventario().adicionarItem(eq);
                    ui.exibirSucesso("Você abriu a Caixa Misteriosa e obteve: " + eq);
                } else {
                    ui.exibirAlerta("Ouro insuficiente.");
                }
            }
        }
    }

    private void descansarNaFogueira() {
        if (jogador.getAtributos().gastarOuro(15)) {
            int curado = jogador.descansar();
            ui.exibirSucesso("Você descansou junto à fogueira e recuperou " + curado + " PV.");
        } else {
            ui.exibirAlerta("Você precisa de 15 de Ouro para acampar com segurança.");
        }
    }

    private void salvarProgresso() {
        try {
            persistenciaService.salvarJogo(jogador, vitoriasTotais, chefesDerrotados);
            ui.exibirSucesso("Progresso salvo com sucesso em 'savegame.json'!");
        } catch (IOException e) {
            ui.exibirPerigo("Erro ao salvar progresso: " + e.getMessage());
        }
    }
}
