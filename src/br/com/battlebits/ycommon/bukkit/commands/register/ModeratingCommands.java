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
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuClickHandler;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuInventory;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuItem;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bukkit.menu.PreferencesMenu;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class ModeratingCommands extends CommandClass {

	private PreferencesMenu menu = new PreferencesMenu();
	
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
		long now = System.currentTimeMillis();
		menu.open(args.getPlayer());
		args.getPlayer().sendMessage("Took " + (System.currentTimeMillis() - now) + "ms");
	}

}
