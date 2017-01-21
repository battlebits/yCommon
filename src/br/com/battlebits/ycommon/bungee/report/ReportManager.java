package br.com.battlebits.ycommon.bungee.report;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.report.Report.ReportInformation;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportManager {

	public static List<Report> getReports() {
		List<Report> reports = new ArrayList<>();
		for (BattlePlayer player : BattlebitsAPI.getAccountCommon().getPlayers()) {
			if (player.getReport() != null)
				reports.add(player.getReport());
		}
		return reports;
	}

	public static boolean addReport(BattlePlayer reported, BattlePlayer player, String reason) {
		Report report = reported.getReport();
		if (report == null) {
			report = reported.newReport();
		}
		if (!report.addReport(player.getUuid(), player.getUserName(), player.getReportPoints(), reason))
			return false;
		reported.setRejectionLevel(reported.getRejectionLevel() + player.getReportPoints());

		return true;
	}

	public static void banPlayer(BattlePlayer banned) {
		if (banned.getReport() == null) {
			return;
		}
		Report report = banned.getReport();
		for (UUID players : report.getPlayersReason().keySet()) {
			BattlePlayer bp = BattlePlayer.getPlayer(players);
			if (bp == null) {
				bp = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(players);
				if (bp == null) {
					break;
				}
			}
			ProxiedPlayer player = BungeeMain.getPlugin().getProxy().getPlayer(bp.getUuid());
			bp.setReportPoints(bp.getReportPoints() + 100);
			if (player != null)
				player.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(bp.getLanguage(), "report-confirmed").replace("%player%", banned.getUserName())));
		}
		banned.clearReport();
		banned.setRejectionLevel(0);
	}

	public static void denyReport(BattlePlayer reported) {
		if (reported.getReport() == null) {
			return;
		}
		Report report = reported.getReport();
		for (Entry<UUID, ReportInformation> players : report.getPlayersReason().entrySet()) {
			BattlePlayer bp = BattlePlayer.getPlayer(players.getKey());
			if (bp == null) {
				bp = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(players.getKey());
				if (bp == null) {
					break;
				}
			}
			bp.setReportPoints(bp.getReportPoints() - 100);
			players.getValue().reject();
			ProxiedPlayer player = BungeeMain.getPlugin().getProxy().getPlayer(bp.getUuid());
			if (player != null)
				player.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(bp.getLanguage(), "report-denied").replace("%player%", reported.getUserName())));
		}
		report.expire();
	}

	public static void mutePlayer(BattlePlayer banned) {
		if (banned.getReport() == null) {
			return;
		}
		Report report = banned.getReport();
		for (UUID players : report.getPlayersReason().keySet()) {
			BattlePlayer bp = BattlePlayer.getPlayer(players);
			if (bp == null) {
				bp = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(players);
				if (bp == null) {
					break;
				}
			}
			ProxiedPlayer player = BungeeMain.getPlugin().getProxy().getPlayer(bp.getUuid());
			bp.setReportPoints(bp.getReportPoints() + 30);
			if (player != null)
				player.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(bp.getLanguage(), "report-confirmed").replace("%player%", banned.getUserName())));
		}
		banned.clearReport();
		banned.setRejectionLevel(0);
	}
}
