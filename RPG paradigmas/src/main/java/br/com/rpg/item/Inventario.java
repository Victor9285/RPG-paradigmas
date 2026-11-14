package br.com.rpg.item;

import br.com.rpg.interfaces.Equipavel;
import br.com.rpg.interfaces.ItemUsavel;
import br.com.rpg.model.Personagem;
import br.com.rpg.model.SlotEquipamento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Inventario {
    private final List<Item> itens;
    private final Map<SlotEquipamento, Equipamento> equipados;

    public Inventario() {
        this.itens = new ArrayList<>();
        this.equipados = new EnumMap<>(SlotEquipamento.class);
    }

    public void adicionarItem(Item item) {
        if (item != null) {
            itens.add(item);
        }
    }

    public boolean removerItem(Item item) {
        return itens.remove(item);
    }

    public String equiparItem(Equipamento novoEquipamento, Personagem portador) {
        if (novoEquipamento == null) {
            return "Equipamento inválido.";
        }
        SlotEquipamento slot = novoEquipamento.getSlot();
        Equipamento anterior = equipados.get(slot);
        if (anterior != null) {
            anterior.aoDesequipar(portador);
            itens.add(anterior);
        }
        itens.remove(novoEquipamento);
        equipados.put(slot, novoEquipamento);
        novoEquipamento.aoEquipar(portador);

        return anterior == null
                ? String.format("%s equipou %s no slot [%s].", portador.getNome(), novoEquipamento.getNome(), slot.getDescricao())
                : String.format("%s substituiu %s por %s no slot [%s].", portador.getNome(), anterior.getNome(), novoEquipamento.getNome(), slot.getDescricao());
    }

    public int getBonusAtaqueTotal() {
        return equipados.values().stream().mapToInt(Equipamento::getBonusAtaque).sum();
    }

    public int getBonusDefesaTotal() {
        return equipados.values().stream().mapToInt(Equipamento::getBonusDefesa).sum();
    }

    public int getBonusSorteTotal() {
        return equipados.values().stream().mapToInt(Equipamento::getBonusSorte).sum();
    }

    public double getBonusEsquivaTotal() {
        return equipados.values().stream().mapToDouble(Equipamento::getBonusEsquiva).sum();
    }

    public List<Item> getItens() {
        return Collections.unmodifiableList(itens);
    }

    public Map<SlotEquipamento, Equipamento> getEquipados() {
        return Collections.unmodifiableMap(equipados);
    }

    public List<Item> getItensUsaveis() {
        return itens.stream().filter(i -> i instanceof ItemUsavel).toList();
    }

    public List<Equipamento> getEquipamentosNaMochila() {
        return itens.stream()
                .filter(i -> i instanceof Equipavel)
                .map(i -> (Equipamento) i)
                .toList();
    }
}
