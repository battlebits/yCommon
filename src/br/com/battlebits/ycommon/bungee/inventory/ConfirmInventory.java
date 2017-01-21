package br.com.battlebits.ycommon.bungee.inventory;

import org.spawl.bungeepackets.inventory.Inventory;
import org.spawl.bungeepackets.inventory.Inventory.ClickHandler;
import org.spawl.bungeepackets.item.ItemStack;
import org.spawl.bungeepackets.item.Material;

import br.com.battlebits.ycommon.bungee.inventory.item.ItemBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ConfirmInventory {

	public ConfirmInventory(ProxiedPlayer player, String confirmTitle, ConfirmHandler handler, Inventory topInventory) {
		Inventory menu = new Inventory(confirmTitle, 54);
		ItemStack nullItem = new ItemBuilder().type(Material.STAINED_GLASS_PANE).durability(15).name(" ").build();
		menu.setItem(0, new ItemBuilder().type(Material.BED).name("§%back%§").build(), new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				if (topInventory != null)
					topInventory.open(arg0);
				else
					menu.close(arg0);
			}
		});

		ClickHandler yesHandler = new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				handler.onCofirm(true);
			}
		};

		ClickHandler noHandler = new ClickHandler() {

			@Override
			public void onClick(ProxiedPlayer arg0, int arg1, ItemStack arg2, boolean arg3, boolean arg4) {
				handler.onCofirm(false);
			}
		};

		menu.setItem(20, new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§%deny%§").build(), noHandler);
		menu.setItem(21, new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§%deny%§").build(), noHandler);
		menu.setItem(29, new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§%deny%§").build(), noHandler);
		menu.setItem(30, new ItemBuilder().type(Material.REDSTONE_BLOCK).name("§%deny%§").build(), noHandler);

		menu.setItem(23, new ItemBuilder().type(Material.EMERALD_BLOCK).name("§%confirm%§").build(), yesHandler);
		menu.setItem(24, new ItemBuilder().type(Material.EMERALD_BLOCK).name("§%confirm%§").build(), yesHandler);
		menu.setItem(32, new ItemBuilder().type(Material.EMERALD_BLOCK).name("§%confirm%§").build(), yesHandler);
		menu.setItem(33, new ItemBuilder().type(Material.EMERALD_BLOCK).name("§%confirm%§").build(), yesHandler);

		for (int i = 0; i < 9; i++) {
			if (menu.getItem(i) == null)
				menu.setItem(i, nullItem);
		}

		for (int i = 45; i < 54; i++) {
			if (menu.getItem(i) == null)
				menu.setItem(i, nullItem);
		}
		menu.open(player);
	}

	public static interface ConfirmHandler {
		public void onCofirm(boolean confirmed);
	}
}
