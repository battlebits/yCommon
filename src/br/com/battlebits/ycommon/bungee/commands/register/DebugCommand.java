package br.com.battlebits.ycommon.bungee.commands.register;

import java.util.Iterator;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.store.iw4.api.ConnectionRecover;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;

public class DebugCommand extends CommandClass {

	@Command(name = "iw4auth", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void iw4auth(CommandArgs cmdArgs) {
		if (ConnectionRecover.auth()) {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Recontectado"));
		} else {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Error"));
		}
	}

	@Command(name = "iw4reconnect", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void iw4reconnect(CommandArgs cmdArgs) {
		if (ConnectionRecover.reconnect()) {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Recontectado"));
		} else {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Ja ta reconectado"));
		}
	}

	@Command(name = "debug", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void debug(CommandArgs cmdArgs) {
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(BattlebitsAPI.getAccountCommon().getPlayers().size() + " players na memoria"));
	}

	@Command(name = "clearcache", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void clearCache(CommandArgs cmdArgs) {
		int playersRemovidos = 0;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (BungeeCord.getInstance().getPlayer(player.getUuid()) == null) {
				players.remove();
				++playersRemovidos;
			}
		}
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText(playersRemovidos + " players removidos do cache"));
	}

	@Command(name = "reloadbungeetranslation", usage = "/<command>", aliases = { "rlbungeetranslations", "rlbungee" }, groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void reloadbungeetranslation(CommandArgs cmdArgs) {
		BungeeMain.getPlugin().loadTranslations();
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Traducoes recarregadas!"));
	}
	
	@Command(name = "reloadreports", usage = "/<command>", groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void reloadreports(CommandArgs cmdArgs) {
		BungeeMain.getPlugin().loadReports();
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Reports recarregados!"));
	}

	@Command(name = "reloadservers", usage = "/<command>", aliases = { "rlservers", "reloadserver", "rlserver" }, groupToUse = Group.DONO, noPermMessageId = "command-no-access")
	public void reloadServers(CommandArgs cmdArgs) {
		BungeeMain.getPlugin().getServerManager().loadServers();
		cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("Servidores recarregados!"));
	}
}
