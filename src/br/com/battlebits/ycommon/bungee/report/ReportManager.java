package br.com.battlebits.ycommon.bungee.report;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportManager {

	private Map<UUID, Report> reports;

	public ReportManager() {
		reports = new HashMap<>();
	}

	public void loadReport(UUID uuid, Report report) {
		reports.put(uuid, report);
	}
	
	public Map<UUID, Report> getReports() {
		return reports;
	}

	public boolean addReport(BattlePlayer reported, BattlePlayer player, String reason) {
		Report report = reports.get(reported.getUuid());
		if (report == null) {
			report = new Report(reported.getUserName(), reported.getUuid());
			reports.put(reported.getUuid(), report);
		}
		if (!report.addReport(player.getUuid(), player.getUserName(), reason))
			return false;
		reported.setRejectionLevel(reported.getRejectionLevel() + player.getReportPoints());
		return true;
	}

	public void banPlayer(BattlePlayer banned) {
		if (!reports.containsKey(banned.getUuid())) {
			return;
		}
		Report report = reports.get(banned.getUuid());
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
		reports.remove(banned.getUuid());
	}
	
	public void denyReport(BattlePlayer reported) {
		if (!reports.containsKey(reported.getUuid())) {
			return;
		}
		Report report = reports.get(reported.getUuid());
		for (UUID players : report.getPlayersReason().keySet()) {
			BattlePlayer bp = BattlePlayer.getPlayer(players);
			if (bp == null) {
				bp = BungeeMain.getPlugin().getAccountManager().loadBattlePlayer(players);
				if (bp == null) {
					break;
				}
			}
			ProxiedPlayer player = BungeeMain.getPlugin().getProxy().getPlayer(bp.getUuid());
			bp.setReportPoints(bp.getReportPoints() - 100);
			if (player != null)
				player.sendMessage(TextComponent.fromLegacyText(Translate.getTranslation(bp.getLanguage(), "report-denied").replace("%player%", reported.getUserName())));
		}
		reports.remove(reported.getUuid());
	}

	public void mutePlayer(BattlePlayer banned) {
		if (!reports.containsKey(banned.getUuid())) {
			return;
		}
		Report report = reports.get(banned.getUuid());
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
		reports.remove(banned.getUuid());
	}

}
