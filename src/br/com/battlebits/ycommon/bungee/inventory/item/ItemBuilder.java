package br.com.battlebits.ycommon.bungee.inventory.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.spawl.bungeepackets.item.Enchantment;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;

public class ItemBuilder {

	private Material material;
	private int amount;
	private short durability;
	private boolean glow;
	private String displayName;
	private HashMap<Enchantment, Integer> enchantments;
	private ArrayList<String> lore;

	public ItemBuilder() {
		material = Material.STONE;
		amount = 1;
		durability = 0;
		glow = false;
	}

	public ItemBuilder type(Material material) {
		this.material = material;
		return this;
	}

	public ItemBuilder amount(int amount) {
		if (amount > 64) {
			amount = 64;
		} else if (amount == 0) {
			amount = 1;
		}
		this.amount = amount;
		return this;
	}

	public ItemBuilder durability(int durability) {
		if (durability >= 0 && durability <= 15) {
			this.durability = (short) durability;
		}
		return this;
	}

	public ItemBuilder name(String text) {
		this.displayName = text.replace("&", "§");
		return this;
	}

	public ItemBuilder enchantment(Enchantment enchantment) {
		return enchantment(enchantment, 1);
	}

	public ItemBuilder enchantment(Enchantment enchantment, Integer level) {
		if (enchantments == null) {
			enchantments = new HashMap<>();
		}
		enchantments.put(enchantment, level);
		return this;
	}

	public ItemBuilder lore(String text) {
		this.lore = new ArrayList<>(StringLoreUtils.getLore(25, text));
		return this;
	}

	public ItemBuilder lore(List<String> text) {
		if (this.lore == null) {
			this.lore = new ArrayList<>();
		}
		for (String str : text) {
			this.lore.add(str.replace("&", "§"));
		}
		return this;
	}

	public ItemBuilder glow() {
		glow = true;
		return this;
	}

	public ItemStack build() {
		ItemStack stack = new ItemStack(material);
		stack.setAmount(amount);
		stack.setData(durability);
		if (enchantments != null && !enchantments.isEmpty()) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				stack.addEnchantment(entry.getKey(), entry.getValue());
			}
		}
		if (displayName != null) {
			stack.setTitle(displayName.replace("&", "§"));
		}
		if (lore != null && !lore.isEmpty()) {
			stack.setLore(lore);
		}
		if (glow && (enchantments == null || enchantments.isEmpty())) {
			stack.addFakeGlow();
		}
		material = Material.STONE;
		amount = 1;
		durability = 0;
		if (glow) {
			glow = false;
		}
		if (displayName != null) {
			displayName = null;
		}
		if (enchantments != null) {
			enchantments.clear();
			enchantments = null;
		}
		if (lore != null) {
			lore.clear();
			lore = null;
		}
		return stack;
	}

}
