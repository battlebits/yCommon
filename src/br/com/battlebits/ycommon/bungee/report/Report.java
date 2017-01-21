package br.com.battlebits.ycommon.bungee.report;

import java.util.HashMap;
import java.util.UUID;

public class Report {

	private String playerName;
	private UUID playerUniqueId;
	private HashMap<UUID, ReportInformation> playersReason;
	private int reportLevel;
	private long reportExpired = Long.MIN_VALUE;
	private UUID lastReport = null;

	public Report(UUID uniqueId, String playerName) {
		playersReason = new HashMap<>();
		reportLevel = 0;
		this.playerName = playerName;
		this.playerUniqueId = uniqueId;
	}

	public boolean addReport(UUID playerReporting, String playerName, int reportLevel, String reason) {
		if (playersReason.containsKey(playerReporting))
			return false;
		playersReason.put(playerReporting, new ReportInformation(playerName, reason, reportLevel));
		this.reportLevel += reportLevel;
		lastReport = playerReporting;
		return true;
	}

	public HashMap<UUID, ReportInformation> getPlayersReason() {
		return playersReason;
	}

	public int getReportLevel() {
		return reportLevel;
	}

	public void setReportLevel(int reportLevel) {
		this.reportLevel = reportLevel;
	}

	public String getPlayerName() {
		return playerName;
	}

	public UUID getPlayerUniqueId() {
		return playerUniqueId;
	}

	public long getLastReportTime() {
		if (lastReport != null) {
			return getPlayersReason().get(lastReport).getReportTime();
		}
		return Long.MIN_VALUE;
	}

	public ReportInformation getLastReport() {
		if (lastReport != null) {
			return getPlayersReason().get(lastReport);
		}
		return null;
	}

	public boolean isExpired() {
		return reportExpired > getLastReportTime();
	}

	public void expire() {
		reportExpired = System.currentTimeMillis();
		reportLevel = 0;
	}

	public static class ReportInformation {
		private String playerName;
		private String reason;
		private long reportTime;
		private int reportLevel;
		private boolean rejected = false;

		public ReportInformation(String playerName, String reason, int reportLevel) {
			this.playerName = playerName;
			this.reason = reason;
			this.reportTime = System.currentTimeMillis();
			this.reportLevel = reportLevel;
		}

		public String getPlayerName() {
			return playerName;
		}

		public String getReason() {
			return reason;
		}
		
		public int getReportLevel() {
			return reportLevel;
		}

		public boolean isRejected() {
			return rejected;
		}

		public long getReportTime() {
			return reportTime;
		}

		public void reject() {
			rejected = true;
		}
	}
}
