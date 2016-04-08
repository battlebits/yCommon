package br.com.battlebits.ycommon.bukkit.api.inventory.menu;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

class MenuHolder implements InventoryHolder {

	private MenuInventory menu;

	public MenuHolder(MenuInventory menuInventory) {
		this.menu = menuInventory;
	}

	public MenuInventory getMenu() {
		return menu;
	}

	public void setMenu(MenuInventory menu) {
		this.menu = menu;
	}

	public void destroy() {
		menu = null;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

}
