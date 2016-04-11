package br.com.battlebits.ycommon.bukkit.commands.register;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuInventory;
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
			if (args.getArgs().length == 0) {
				args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-help"));
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
							args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-changed-you").replace("%gamemode%",
									Translate.getTranslation(bp.getLanguage(), "gamemode-name-" + gm.name().toLowerCase())));
						} else {
							args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args.getArgs()[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-changed-other").replace(
										"%gamemode%", Translate.getTranslation(bp.getLanguage(), "gamemode-name-" + gm.name().toLowerCase())));
							} else {
								args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-already-other"));
							}
						} else {
							args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-player-notfound"));
						}
						t = null;
					}
				} else {
					args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-unknown"));
				}
				gm = null;
			}
			bp = null;
		}
	}

	@Command(name = "tp", aliases = { "teleport", "teleportar" }, groupToUse = Group.NORMAL, noPermMessageId = "teleport-no-access")
	public void tp(CommandArgs args) {
		ItemStack stack = new ItemStack(Material.ANVIL, 1);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("%translateId:gamemode-player-notfound%");
		meta.setLore(Arrays.asList("%translateId:gamemode-changed-you%", "Oi", "%translateId:gamemode-changed-other%"));
		stack.setItemMeta(meta);
		args.getPlayer().getInventory().addItem(stack);
	}

}
