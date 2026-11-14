package br.com.rpg.interfaces;

import br.com.rpg.model.EntidadeCombate;
import br.com.rpg.model.ResultadoAtaque;
import br.com.rpg.rng.GeradorAleatorio;

/**
 * Interface Segregada (ISP): Define o contrato para técnicas especiais de cada classe ou chefe.
 */
public interface HabilidadeEspecial {
    String getNomeHabilidade();
    ResultadoAtaque usarHabilidadeEspecial(EntidadeCombate alvo, GeradorAleatorio rng);
}
