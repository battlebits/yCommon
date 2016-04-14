package br.com.battlebits.ycommon.bukkit.menu;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.battlebits.ycommon.bukkit.api.inventory.menu.ClickType;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuClickHandler;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuInventory;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuItem;
import br.com.battlebits.ycommon.bukkit.api.item.ItemBuilder;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class PreferencesMenu {

	private ItemStack conversationItem;
	private MenuItem conversationIcon;
	private ItemStack tellEnabledItem;
	private ItemStack tellDisabledItem;
	private MenuItem tellEnabledIcon;
	private MenuItem tellDisabledIcon;

	public PreferencesMenu() {
		ItemBuilder builder = new ItemBuilder();
		conversationItem = builder.amount(1).type(Material.PAPER).name("§6%msgId:item-preferences-conversation-icon-name%")
				.lore(Arrays.asList("§8%msgId:item-preferences-icon-name%", "§0", "§7%msgId:item-preferences-conversation-icon-lore%", "§0",
						"§e» %msgId:item-preferences-icon-edit-name%"))
				.build();
		tellEnabledItem = builder.amount(1).type(Material.BOOK_AND_QUILL).glow().name("§a%msgId:item-preferences-conversation-tell-name%")
				.lore(Arrays.asList("§8%msgId:item-preferences-icon-name%", "§0", "§7%msgId:item-preferences-conversation-tell-lore%", "§0",
						"§e» %msgId:item-preferences-icon-edit-disable%"))
				.build();
		tellDisabledItem = builder.amount(1).type(Material.BOOK_AND_QUILL).name("§a%msgId:item-preferences-conversation-tell-name%")
				.lore(Arrays.asList("§8%msgId:item-preferences-icon-name%", "§0", "§7%msgId:item-preferences-conversation-tell-lore%", "§0",
						"§e» %msgId:item-preferences-icon-edit-enable%"))
				.build();
		conversationIcon = new MenuItem(conversationItem, new MenuClickHandler() {

			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				openConversation(p);
				p = null;
			}
		});
		MenuClickHandler tellClickHandler = new MenuClickHandler() {
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				if (bp.getConfiguration().isTellEnabled()) {
					bp.getConfiguration().setTellEnabled(false);
					inv.setItem(slot, tellDisabledItem);
				} else {
					bp.getConfiguration().setTellEnabled(true);
					inv.setItem(slot, tellEnabledItem);
				}
				bp = null;
			}
		};
		tellEnabledIcon = new MenuItem(tellEnabledItem, tellClickHandler);
		tellDisabledIcon = new MenuItem(tellDisabledItem, tellClickHandler);
		tellClickHandler = null;
	}

	public void open(Player p) {
		MenuInventory menu = new MenuInventory("%msgId:menu-preferences-name%", 5, true);
		menu.setItem(conversationIcon, 10);
		menu.open(p);
		menu = null;
		p = null;
	}

	public void openConversation(Player p) {
		MenuInventory menu = new MenuInventory("%msgId:menu-p-conversation-name%", 3, true);
		BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		if (bp.getConfiguration().isTellEnabled()) {
			menu.setItem(tellEnabledIcon, 10);
		} else {
			menu.setItem(tellDisabledIcon, 10);
		}
		menu.open(p);
		menu = null;
		bp = null;
		p = null;
	}

}
