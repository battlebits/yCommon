package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.Arrays;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.bukkit.api.inventory.menu.ClickType;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuInventory;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuItem;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.clickhandler.MenuClickHandler;
import br.com.battlebits.ycommon.bukkit.commands.CommandClass;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class ModeratingCommands extends CommandClass {

	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.ADMIN, noPermMessageId = "gamemode-no-access")
	public void gamemode(CommandArgs args) {
		if (args.isPlayer()) {
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-gamemode-prefix");
			if (args.getArgs().length == 0) {
				args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-help"));
			} else {
				GameMode gm = null;
				try {
					gm = GameMode.valueOf(args.getArgs()[0].toUpperCase());
				} catch (Exception e) {
					try {
						gm = GameMode.getByValue(Integer.parseInt(args.getArgs()[0]));
					} catch (Exception ex) {
					}
				}
				if (gm != null) {
					if (args.getArgs().length == 1) {
						if (args.getPlayer().getGameMode() != gm) {
							args.getPlayer().setGameMode(gm);
							args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-you")
									.replace("%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
						} else {
							args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args.getArgs()[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-other").replace(
										"%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
							} else {
								args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-other"));
							}
						} else {
							args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-player-notfound"));
						}
						t = null;
					}
				} else {
					args.getPlayer().sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-unknown"));
				}
				gm = null;
			}
			prefix = null;
			bp = null;
		}
	}

	@Command(name = "tp", aliases = { "teleport", "teleportar" }, groupToUse = Group.TRIAL, noPermMessageId = "command-teleport-no-access")
	public void tp(CommandArgs args) {
		ItemStack stack = new ItemStack(Material.ANVIL, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("%msgId:gamemode-player-notfound%");
		meta.setLore(Arrays.asList("%msgId:gamemode-changed-you%", "Oi", "%msgId:gamemode-changed-other%"));
		stack.setItemMeta(meta);
		MenuInventory menu = new MenuInventory("%msgId:gamemode-player-notfound%", 1, false);
		menu.setItem(new MenuItem(stack, new MenuClickHandler() {

			@Override
			public void onClick(Player p, MenuInventory menu, ClickType type, ItemStack stack) {
				p.sendMessage(stack.getItemMeta().getDisplayName());
				stack.setType(Material.values()[new Random().nextInt(Material.values().length - 1)]);
			}
		}), 1);
		menu.open(args.getPlayer());
	}

}
