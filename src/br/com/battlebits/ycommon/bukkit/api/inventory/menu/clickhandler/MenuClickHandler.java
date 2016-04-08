package br.com.battlebits.ycommon.bukkit.api.inventory.menu.clickhandler;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ycommon.bukkit.api.inventory.menu.ClickType;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuInventory;

public interface MenuClickHandler {

	public void onClick(Player p, MenuInventory menu, ClickType type, ItemStack stack);

}
