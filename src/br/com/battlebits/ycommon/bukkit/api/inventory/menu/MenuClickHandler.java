package br.com.battlebits.ycommon.bukkit.api.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface MenuClickHandler {

	public void onClick(Player p, MenuInventory menu, ClickType type, ItemStack stack);

}
