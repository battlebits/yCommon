package br.com.battlebits.ycommon.bungee.commands.register;

import org.spawl.bungeepackets.BungeePackets;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.Command;
import br.com.battlebits.ycommon.bungee.commands.BungeeCommandFramework.CommandArgs;
import br.com.battlebits.ycommon.bungee.inventory.ReportListInventory;
import br.com.battlebits.ycommon.bungee.report.ReportManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.commandmanager.CommandClass;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportCommand extends CommandClass {

	@Command(name = "report", usage = "/<command>")
	public void report(CommandArgs cmdArgs) {
		if (!cmdArgs.isPlayer()) {
			cmdArgs.getSender().sendMessage(TextComponent.fromLegacyText("COMANDO APENAS PARA PLAYERS"));
			return;
		}
		boolean test = false;
		String[] args = cmdArgs.getArgs();
		BattlePlayer player = BattlePlayer.getPlayer(cmdArgs.getPlayer().getUniqueId());
		if ((player.hasGroupPermission(Group.TRIAL) && args.length == 0 && test) || (!test && player.hasGroupPermission(Group.TRIAL))) {
			new ReportListInventory(cmdArgs.getPlayer(), 1);
			return;
		}
		Language lang = BattlebitsAPI.getDefaultLanguage();
		CommandSender sender = cmdArgs.getSender();
		if (cmdArgs.isPlayer()) {
			lang = BattlebitsAPI.getAccountCommon().getBattlePlayer(cmdArgs.getPlayer().getUniqueId()).getLanguage();
		}
		String reportPrefix = Translate.getTranslation(lang, "command-report-prefix") + " ";
		if (args.length < 2) {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "command-report-usage").replace("%command%", cmdArgs.getLabel())));
			return;
		}
		ProxiedPlayer proxied = BungeeMain.getPlugin().getProxy().getPlayer(args[0]);
		if (proxied == null) {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "player-not-online")));
			return;
		}
		BattlePlayer reported = BattlePlayer.getPlayer(proxied.getUniqueId());
		if (reported.hasGroupPermission(Group.STAFF) && !test) {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "command-report-cant-staff")));
			return;
		}
		if (reported.getUuid().equals(player.getUuid()) && !test) {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "cant-yourself")));
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			String espaco = " ";
			if (i >= args.length - 1)
				espaco = "";
			builder.append(args[i] + espaco);
		}
		if (ReportManager.addReport(reported, player, builder.toString())) {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "command-report-success")));
		} else {
			sender.sendMessage(TextComponent.fromLegacyText(reportPrefix + Translate.getTranslation(lang, "command-report-already-reported")));
		}

		for (ProxiedPlayer proxiedP : BungeeCord.getInstance().getPlayers()) {
			BattlePlayer pl = BattlebitsAPI.getAccountCommon().getBattlePlayer(proxiedP.getUniqueId());
			if (pl.hasGroupPermission(Group.TRIAL)) {
				BungeePackets.playSound(proxiedP, "mob.bat.hurt1", 1f, 1f);
			}
		}
	}
}
