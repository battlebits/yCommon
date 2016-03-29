package br.com.battlebits.ycommon.bukkit.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class ModeratingCommands {

	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.ADMIN, noPermMessageId = "gamemode-no-access")
	public void gamemode(CommandArgs args) {
		if (args.isPlayer()) {
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
			if (args.getArgs().length == 0) {
				Translate.getTranslation(bp.getLanguage(), "gamemode-help");
			} else {
				GameMode gm = GameMode.valueOf(args.getArgs()[0].toUpperCase());
				if (gm == null) {
					try {
						gm = GameMode.getByValue(Integer.parseInt(args.getArgs()[0]));
					} catch (Exception e) {
					}
				}
				if (gm != null) {
					if (args.getArgs().length == 1) {
						if (args.getPlayer().getGameMode() != gm) {
							args.getPlayer().setGameMode(gm);
							args.getPlayer()
									.sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-changed-you").replace("%gamemode%", gm.name()));
						} else {
							args.getPlayer().sendMessage(Translate.getTranslation(bp.getLanguage(), "gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args.getArgs()[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								args.getPlayer().sendMessage(
										Translate.getTranslation(bp.getLanguage(), "gamemode-changed-you").replace("%gamemode%", gm.name()));
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
	
	@Command(name = "tp", aliases = {"teleport", "teleportar"}, groupToUse = Group.TRIAL, noPermMessageId = "teleport-no-access")
	public void tp(CommandArgs args) {
		
	}

}
