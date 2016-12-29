package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerCommand extends CommandClass {

	@Command(name = "connect", usage = "/<command> <server>", aliases = { "server", "con" })
	public void connect(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();

			if (cmdArgs.getArgs().length != 1) {
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "command-connect-usage")));
				return;
			}
			String serverIp = cmdArgs.getArgs()[0];

			BattleServer server = BungeeMain.getPlugin().getServerManager().getServer(serverIp);
			if (server != null && server.getServerInfo() != null) {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connect-ip").replace("%address%", serverIp.toLowerCase())));
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.connect(server.getServerInfo());
			} else {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-exists")));
				p.sendMessage(TextComponent.fromLegacyText(""));
			}
		}
	}

	@Command(name = "hungergames", usage = "/<command>", aliases = { "hg" })
	public void hungergames(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			BattleServer hg = BungeeMain.getPlugin().getServerManager().getHgBalancer().next();
			if (hg != null && hg.getServerInfo() != null) {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connect-hungergames")));
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.connect(hg.getServerInfo());
			} else {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-available")));
				p.sendMessage(TextComponent.fromLegacyText(""));
			}
		}
	}
	
	@Command(name = "doublekit", usage = "/<command>", aliases = { "dk" })
	public void doublekit(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			BattleServer hg = BungeeMain.getPlugin().getServerManager().getDoubleKitHGBalancer().next();
			if (hg != null && hg.getServerInfo() != null) {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connect-doublekit")));
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.connect(hg.getServerInfo());
			} else {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-available")));
				p.sendMessage(TextComponent.fromLegacyText(""));
			}
		}
	}
	
	@Command(name = "customhg", usage = "/<command>", aliases = { "custom" })
	public void customhg(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			BattleServer hg = BungeeMain.getPlugin().getServerManager().getCustomHgBalancer().next();
			if (hg != null && hg.getServerInfo() != null) {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connect-customhg")));
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.connect(hg.getServerInfo());
			} else {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-available")));
				p.sendMessage(TextComponent.fromLegacyText(""));
			}
		}
	}

	@Command(name = "fairplay", usage = "/<command>", aliases = { "fp" })
	public void fairplay(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			p.sendMessage(TextComponent.fromLegacyText(""));
			p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-available")));
			p.sendMessage(TextComponent.fromLegacyText(""));
		}
	}

	@Command(name = "lobby", usage = "/<command>", aliases = { "hub" })
	public void lobby(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			BattleServer lobby = BungeeMain.getPlugin().getServerManager().getLobbyBalancer().next();
			if (lobby != null && lobby.getServerInfo() != null) {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connect-lobby")));
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.connect(lobby.getServerInfo());
			} else {
				p.sendMessage(TextComponent.fromLegacyText(""));
				p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-not-available")));
				p.sendMessage(TextComponent.fromLegacyText(""));
			}
		}
	}

	@Command(name = "address", usage = "/<command>", aliases = { "ip" })
	public void address(CommandArgs cmdArgs) {
		if (cmdArgs.isPlayer()) {
			ProxiedPlayer p = cmdArgs.getPlayer();
			p.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId()).getLanguage(), "server-connected-ip").replace("%address%", p.getServer().getInfo().getName().toUpperCase())));
		}
	}

}
