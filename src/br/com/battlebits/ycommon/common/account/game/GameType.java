package br.com.battlebits.ycommon.common.account.game;

public enum GameType {
	BATTLECRAFT_PVP_STATUS("bc_pvp_status"), //
	RAID_ACCOUNT("raid_account");

	private String serverId;

	private GameType(String str) {
		serverId = str;
	}

	public String getServerId() {
		return serverId;
	}
}
