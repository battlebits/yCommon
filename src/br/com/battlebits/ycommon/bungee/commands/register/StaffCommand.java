package br.com.battlebits.ycommon.bungee.commands.register;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class StaffCommand extends CommandClass {

	@Command(name = "staffchat", aliases = { "sc" }, groupToUse = Group.STAFF, noPermMessageId = "command-staffchat-no-access")
	public void staffchat(CommandArgs args) {
		if (!args.isPlayer()) {
			args.getSender().sendMessage(TextComponent.fromLegacyText("COMANDO APENAS PARA PLAYERS"));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(args.getPlayer().getUniqueId());
		boolean active = player.getConfiguration().isStaffChatEnabled();
		player.getConfiguration().setStaffChatEnabled(!active);
		args.getSender().sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(player.getLanguage(), "command-staffchat-" + (active ? "disabled" : "enabled"))));
	}

	@Command(name = "screeshare", aliases = { "ss" }, groupToUse = Group.MODPLUS, noPermMessageId = "command-screeshare-no-access")
	public void screeshare(CommandArgs cmdArgs) {
		if (!cmdArgs.isPlayer()) {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("COMANDO APENAS PARA PLAYERS"));
			return;
		}
		CommandSender sender = cmdArgs.getSender();
		String[] args = cmdArgs.getArgs();
		Language lang = BattlebitsAPI.getDefaultLanguage();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		String ssPrefix = Translate.getTranslation(lang, "command-screeshare-prefix") + " ";
		if (args.length < 2) {
			sender.sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(lang, "command-screeshare-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		ProxiedPlayer proxied = BungeeMain.getPlugin().getProxy().getPlayer(args[0]);
		if (proxied == null) {
			sender.sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(lang, "player-not-online")));
			return;
		}
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxied.getUniqueId());
		if (player.isScreensharing()) {
			player.setScreensharing(false);
			proxied.sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(player.getLanguage(), "command-screenshare-finished")));
			if (player.getLastServer().isEmpty()) {
				proxied.connect(BungeeMain.getPlugin().getServerManager().getLobbyBalancer().next().getServerInfo());
			} else {
				proxied.connect(BungeeMain.getPlugin().getProxy().getServerInfo(player.getLastServer()));
			}
			return;
		}
		ServerInfo server = BungeeMain.getPlugin().getProxy().getServerInfo("ss.battlebits.com.br");

		if (server == null) {
			cmdArgs.getPlayer().sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(BattlePlayer.getLanguage(cmdArgs.getPlayer().getUniqueId()), "command-screenshare-server-not-online")));
			return;
		}

		player.setScreensharing(true);

		proxied.sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(player.getLanguage(), "command-screenshare-started")));
		cmdArgs.getPlayer().sendMessage(TextComponent.fromLegacyText(ssPrefix + Translate.getTranslation(BattlePlayer.getLanguage(cmdArgs.getPlayer().getUniqueId()), "command-screenshare-moderator")));

		cmdArgs.getPlayer().connect(server);
		proxied.connect(server);
	}

}
