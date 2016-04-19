package br.com.battlebits.ycommon.bukkit.commands.register;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.Command;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commands.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class ModeratingCommands extends CommandClass {

	@SuppressWarnings("deprecation")
	@Command(name = "gamemode", aliases = { "gm" }, groupToUse = Group.ADMIN, noPermMessageId = "gamemode-no-access")
	public void gamemode(CommandArgs command) {
		if (command.isPlayer()) {
			Player p = command.getPlayer();
			String[] args = command.getArgs();
			BattlePlayer bp = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			String prefix = Translate.getTranslation(bp.getLanguage(), "command-gamemode-prefix");
			if (args.length == 0) {
				p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-help"));
			} else {
				GameMode gm = null;
				try {
					gm = GameMode.valueOf(args[0].toUpperCase());
				} catch (Exception e) {
					try {
						gm = GameMode.getByValue(Integer.parseInt(args[0]));
					} catch (Exception ex) {
					}
				}
				if (gm != null) {
					if (args.length == 1) {
						if (p.getGameMode() != gm) {
							p.setGameMode(gm);
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-you").replace("%gamemode%",
									Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-you"));
						}
					} else {
						Player t = Bukkit.getPlayer(args[1]);
						if (t != null) {
							if (t.getGameMode() != gm) {
								t.setGameMode(gm);
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-changed-other").replace(
										"%gamemode%", Translate.getTranslation(bp.getLanguage(), "name-gamemode-" + gm.name().toLowerCase())));
							} else {
								p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-already-other"));
							}
						} else {
							p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-player-notfound"));
						}
						t = null;
					}
				} else {
					p.sendMessage(prefix + Translate.getTranslation(bp.getLanguage(), "command-gamemode-unknown"));
				}
				gm = null;
			}
			prefix = null;
			bp = null;
			args = null;
			p = null;
		} else {
			command.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

	@Command(name = "tp", aliases = { "teleport", "teleportar" }, groupToUse = Group.TRIAL, noPermMessageId = "command-teleport-no-access")
	public void tp(CommandArgs command) {
		if (command.isPlayer()) {
			Player p = command.getPlayer();
			String[] args = command.getArgs();
			args = null;
			p = null;
		} else {
			command.getSender().sendMessage("§4§lERRO §fComando disponivel apenas §c§lin-game");
		}
	}

}
