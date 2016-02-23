package br.com.battlebits.yutils.common.banmanager.constructors;

import java.util.UUID;

public class Ban {

	private UUID bannedPlayer;
	private String bannedBy;
	private String bannedIp;

	private UUID bannedByUUID;
	private long banTime;
	private String reason;

	private boolean unbanned;
	private String unbannedBy;
	private UUID unbannedByUUID;

	private long expire;
	private long duration;

	public Ban(UUID bannedPlayer, String bannedBy, String bannedIp, UUID bannedByUUID, long banTime, String reason, boolean unbanned, String unbannedBy, UUID unbannedByUUID, long expire, long duration) {
		this.bannedPlayer = bannedPlayer;
		this.bannedBy = bannedBy;
		this.bannedIp = bannedIp;
		this.bannedByUUID = bannedByUUID;
		this.banTime = banTime;
		this.reason = reason;
		this.unbanned = unbanned;
		this.unbannedBy = unbannedBy;
		this.unbannedByUUID = unbannedByUUID;
		this.expire = expire;
		this.duration = duration;
	}

	public UUID getBannedPlayer() {
		return bannedPlayer;
	}

	public String getBannedBy() {
		return bannedBy;
	}

	public String getBannedIp() {
		return bannedIp;
	}

	public UUID getBannedByUUID() {
		return bannedByUUID;
	}

	public long getBanTime() {
		return banTime;
	}

	public String getReason() {
		return reason;
	}

	public boolean isUnbanned() {
		return unbanned;
	}

	public String getUnbannedBy() {
		return unbannedBy;
	}

	public UUID getUnbannedByUUID() {
		return unbannedByUUID;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return duration;
	}

}
