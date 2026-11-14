package br.com.rpg.service;

import br.com.rpg.item.ElixirSorte;
import br.com.rpg.item.Equipamento;
import br.com.rpg.item.Item;
import br.com.rpg.item.PocaoCura;
import br.com.rpg.model.Arqueiro;
import br.com.rpg.model.Atributos;
import br.com.rpg.model.Guerreiro;
import br.com.rpg.model.Mago;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.Raridade;
import br.com.rpg.model.SlotEquipamento;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsabilidade Única (SRP): Persistência do progresso do jogador em JSON via Gson.
 */
public class PersistenciaService {
    private final Path caminhoArquivo;
    private final Gson gson;

    public PersistenciaService(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public void salvarJogo(Personagem jogador, int vitoriasTotais, int chefesDerrotados) throws IOException {
        SaveData data = new SaveData();
        data.classe = jogador.getNomeClasse();
        data.nome = jogador.getNome();
        data.atributos = jogador.getAtributos();
        data.vitoriasTotais = vitoriasTotais;
        data.chefesDerrotados = chefesDerrotados;

        for (Item item : jogador.getInventario().getItens()) {
            data.itensMochila.add(converterParaDto(item));
        }
        for (Equipamento eq : jogador.getInventario().getEquipados().values()) {
            data.equipamentosAtivos.add(converterParaDto(eq));
        }

        String json = gson.toJson(data);
        Files.writeString(caminhoArquivo, json);
    }

    public EstadoCarregado carregarJogo() throws IOException {
        if (!Files.exists(caminhoArquivo)) {
            return null;
        }
        String json = Files.readString(caminhoArquivo);
        SaveData data = gson.fromJson(json, SaveData.class);
        if (data == null || data.atributos == null) {
            return null;
        }

        Personagem personagem = switch (data.classe) {
            case "Guerreiro" -> new Guerreiro(data.nome);
            case "Mago" -> new Mago(data.nome);
            default -> new Arqueiro(data.nome);
        };

        Atributos orig = data.atributos;
        Atributos dest = personagem.getAtributos();
        dest.setVidaMaxima(orig.getVidaMaxima());
        dest.setVidaAtual(orig.getVidaAtual());
        dest.setAtaque(orig.getAtaque());
        dest.setDefesa(orig.getDefesa());
        dest.setSorte(orig.getSorte());
        dest.setEsquivaBase(orig.getEsquivaBase());
        dest.setChanceCriticoBase(orig.getChanceCriticoBase());
        dest.setNivel(orig.getNivel());
        dest.setExperiencia(orig.getExperiencia());
        dest.setExperienciaProximoNivel(orig.getExperienciaProximoNivel());
        dest.setOuro(orig.getOuro());

        for (ItemDTO dto : data.itensMochila) {
            Item item = reconstruirItem(dto);
            if (item != null) {
                personagem.getInventario().adicionarItem(item);
            }
        }
        for (ItemDTO dto : data.equipamentosAtivos) {
            Item item = reconstruirItem(dto);
            if (item instanceof Equipamento eq) {
                personagem.getInventario().equiparItem(eq, personagem);
            }
        }

        return new EstadoCarregado(personagem, data.vitoriasTotais, data.chefesDerrotados);
    }

    public boolean existeSave() {
        return Files.exists(caminhoArquivo);
    }

    private ItemDTO converterParaDto(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.nome = item.getNome();
        dto.raridade = item.getRaridade().name();
        dto.valorOuro = item.getValorOuro();
        if (item instanceof PocaoCura pocao) {
            dto.tipo = "POCAO";
            dto.valorPrincipal = pocao.getPontosCura();
        } else if (item instanceof ElixirSorte elixir) {
            dto.tipo = "ELIXIR";
            dto.valorPrincipal = elixir.getBonusSortePermanente();
        } else if (item instanceof Equipamento eq) {
            dto.tipo = "EQUIPAMENTO";
            dto.slot = eq.getSlot().name();
            dto.bonusAtaque = eq.getBonusAtaque();
            dto.bonusDefesa = eq.getBonusDefesa();
            dto.bonusSorte = eq.getBonusSorte();
            dto.bonusEsquiva = eq.getBonusEsquiva();
        }
        return dto;
    }

    private Item reconstruirItem(ItemDTO dto) {
        Raridade raridade = Raridade.valueOf(dto.raridade);
        return switch (dto.tipo) {
            case "POCAO" -> new PocaoCura(dto.nome, raridade, dto.valorPrincipal, dto.valorOuro);
            case "ELIXIR" -> new ElixirSorte(dto.nome, raridade, dto.valorPrincipal, dto.valorOuro);
            case "EQUIPAMENTO" -> new Equipamento(
                    dto.nome, raridade, SlotEquipamento.valueOf(dto.slot),
                    dto.bonusAtaque, dto.bonusDefesa, dto.bonusSorte, dto.bonusEsquiva, dto.valorOuro
            );
            default -> null;
        };
    }

    public record EstadoCarregado(Personagem personagem, int vitoriasTotais, int chefesDerrotados) {
    }

    private static class SaveData {
        String classe;
        String nome;
        Atributos atributos;
        int vitoriasTotais;
        int chefesDerrotados;
        List<ItemDTO> itensMochila = new ArrayList<>();
        List<ItemDTO> equipamentosAtivos = new ArrayList<>();
    }

    private static class ItemDTO {
        String tipo;
        String nome;
        String raridade;
        int valorOuro;
        int valorPrincipal;
        String slot;
        int bonusAtaque;
        int bonusDefesa;
        int bonusSorte;
        double bonusEsquiva;
    }
}
