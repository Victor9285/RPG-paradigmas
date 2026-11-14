package br.com.rpg.ui;

import br.com.rpg.model.Inimigo;
import br.com.rpg.model.Personagem;

/**
 * Princípio da Inversão de Dependência (DIP):
 * Desacopla a lógica do jogo da entrada/saída concreta (console, interface gráfica ou mocks de teste).
 */
public interface InterfaceUsuario {
    void exibirTitulo(String titulo);
    void exibirMensagem(String mensagem);
    void exibirSucesso(String mensagem);
    void exibirAlerta(String mensagem);
    void exibirPerigo(String mensagem);
    void exibirPainelCombate(Personagem jogador, Inimigo inimigo, int turno);
    void exibirFichaCompleta(Personagem jogador);
    int lerOpcao(String prompt, int min, int max);
    String lerTexto(String prompt);
}
