package br.com.battlebits.ycommon.bukkit.api.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;

public class ItemBuilder {

	private Material material;
	private int amount;
	private short durability;
	private boolean useMeta;
	private boolean glow;
	private String displayName;
	private HashMap<Enchantment, Integer> enchantments;
	private ArrayList<String> lore;
	private NBTTagCompound basicNBT;
	private NBTTagList enchNBT;

	public ItemBuilder() {
		material = Material.STONE;
		amount = 1;
		durability = 0;
		useMeta = false;
		glow = false;
		basicNBT = new NBTTagCompound();
		enchNBT = new NBTTagList();
		basicNBT.set("ench", enchNBT);
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
		if (!useMeta) {
			useMeta = true;
		}
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
		if (!this.useMeta) {
			this.useMeta = true;
		}
		this.lore = new ArrayList<>(StringLoreUtils.getLore(20, text));
		return this;
	}

	public ItemBuilder lore(List<String> text) {
		if (!this.useMeta) {
			this.useMeta = true;
		}
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
		stack.setDurability(durability);
		if (enchantments != null && !enchantments.isEmpty()) {
			for (Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
				stack.addEnchantment(entry.getKey(), entry.getValue());
			}
		}
		if (useMeta) {
			ItemMeta meta = stack.getItemMeta();
			if (displayName != null) {
				meta.setDisplayName(displayName.replace("&", "§"));
			}
			if (lore != null && !lore.isEmpty()) {
				meta.setLore(lore);
			}
			stack.setItemMeta(meta);
		}
		if (glow && (enchantments == null || enchantments.isEmpty())) {
			net.minecraft.server.v1_7_R4.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
			if (nmsStack.hasTag()) {
				nmsStack.getTag().set("ench", enchNBT);
			} else {
				nmsStack.setTag(basicNBT);
			}
			stack = CraftItemStack.asCraftMirror(nmsStack);
		}
		material = Material.STONE;
		amount = 1;
		durability = 0;
		if (useMeta) {
			useMeta = false;
		}
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
