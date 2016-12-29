package br.com.battlebits.ycommon.bungee.report;

import java.util.HashMap;
import java.util.UUID;

public class Report {

	private String playerName;
	private UUID playerUniqueId;
	private HashMap<UUID, ReportInformation> playersReason;
	private int reportLevel;
	private long lastReportTime;
	
	public Report(String playerName, UUID uuid) {
		playersReason = new HashMap<>();
		reportLevel = 0;
		this.playerName = playerName;
		this.playerUniqueId = uuid;
	}

	public boolean addReport(UUID playerReporting, String playerName, String reason) {
		if (playersReason.containsKey(playerReporting))
			return false;
		playersReason.put(playerReporting, new ReportInformation(playerName, reason));
		lastReportTime = System.currentTimeMillis();
		return true;
	}

	public HashMap<UUID, ReportInformation> getPlayersReason() {
		return playersReason;
	}

	public int getReportLevel() {
		return reportLevel;
	}
	
	public String getPlayerName() {
		return playerName;
	}
	
	public UUID getPlayerUniqueId() {
		return playerUniqueId;
	}
	
	public long getLastReportTime() {
		return lastReportTime;
	}

	public static class ReportInformation {
		private String playerName;
		private String reason;

		public ReportInformation(String playerName, String reason) {
			this.playerName = playerName;
			this.reason = reason;
		}

		public String getPlayerName() {
			return playerName;
		}

		public String getReason() {
			return reason;
		}

	}
}
