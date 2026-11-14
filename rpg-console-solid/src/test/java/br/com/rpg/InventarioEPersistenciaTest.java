package br.com.rpg;

import br.com.rpg.item.ElixirSorte;
import br.com.rpg.item.Equipamento;
import br.com.rpg.model.Arqueiro;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.SlotEquipamento;
import br.com.rpg.service.PersistenciaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class InventarioEPersistenciaTest {

    @Test
    void deveSomarBonusDeEquipamentoEElixirDeSorte(@TempDir Path tempDir) throws Exception {
        Personagem arqueiro = new Arqueiro("Shadow");
        int sorteInicial = arqueiro.getSorteTotal();
        double esquivaInicial = arqueiro.getChanceEsquivaTotal();

        Equipamento amuleto = new Equipamento("Talismã Astral", Raridade.EPICO, SlotEquipamento.AMULETO, 2, 2, 10, 5.0, 100);
        arqueiro.getInventario().equiparItem(amuleto, arqueiro);

        ElixirSorte elixir = new ElixirSorte("Elixir Supremo", Raridade.LENDARIO, 5, 80);
        elixir.usar(arqueiro);

        assertEquals(sorteInicial + 15, arqueiro.getSorteTotal());
        assertTrue(arqueiro.getChanceEsquivaTotal() > esquivaInicial);

        Path savePath = tempDir.resolve("test-save.json");
        PersistenciaService persistencia = new PersistenciaService(savePath);
        persistencia.salvarJogo(arqueiro, 4, 1);

        var carregado = persistencia.carregarJogo();
        assertNotNull(carregado);
        assertEquals("Shadow", carregado.personagem().getNome());
        assertEquals(4, carregado.vitoriasTotais());
        assertEquals(1, carregado.chefesDerrotados());
        assertEquals(arqueiro.getSorteTotal(), carregado.personagem().getSorteTotal());
    }
}
