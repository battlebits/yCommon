package br.com.battlebits.ycommon.bukkit.api.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ycommon.bukkit.api.inventory.menu.clickhandler.MenuClickHandler;

public class MenuItem {

	private ItemStack stack;
	private MenuClickHandler handler;

	public MenuItem(ItemStack itemstack) {
		this.stack = itemstack;
		this.handler = new MenuClickHandler() {
			@Override
			public void onClick(Player p, MenuInventory menu, ClickType type, ItemStack stack) {}
		};
	}

	public MenuItem(ItemStack itemstack, MenuClickHandler clickHandler) {
		this.stack = itemstack;
		this.handler = clickHandler;
	}

	public ItemStack getStack() {
		return stack;
	}

	public MenuClickHandler getHandler() {
		return handler;
	}

	public void destroy() {
		stack = null;
		handler = null;
	}

}