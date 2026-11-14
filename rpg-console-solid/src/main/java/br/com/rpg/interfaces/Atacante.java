package br.com.rpg.interfaces;

import br.com.rpg.model.EntidadeCombate;
import br.com.rpg.model.ResultadoAtaque;
import br.com.rpg.rng.GeradorAleatorio;

/**
 * Interface Segregada (ISP): Define exclusivamente o contrato para entidades capazes de realizar ataques.
 */
public interface Atacante {
    ResultadoAtaque atacar(EntidadeCombate alvo, GeradorAleatorio rng);
}
