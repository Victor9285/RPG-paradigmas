package br.com.rpg.interfaces;

import br.com.rpg.rng.GeradorAleatorio;

/**
 * Interface Segregada (ISP): Define o contrato de esquiva dinâmica influenciada por agilidade e sorte (Luck).
 */
public interface Esquivavel {
    boolean tentarEsquivar(GeradorAleatorio rng);
    double getChanceEsquivaTotal();
}
