package br.com.battlebits.ycommon.common.banmanager.constructors;

import java.util.UUID;

import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class Ban {

	private String bannedBy;
	private UUID bannedByUUID;
	private String bannedIp;

	private String server;

	private long banTime;
	private String reason;

	private boolean unbanned;
	private String unbannedBy;
	private UUID unbannedByUUID;
	private long unbanTime;

	private long expire;
	private long duration;

	public Ban(String bannedBy, String bannedIp, String server, String reason, long duration) {
		this(bannedBy, null, bannedIp, server, reason, duration);
	}

	public Ban(String bannedBy, UUID bannedByUuid, String bannedIp, String server, String reason, long expire) {
		this(bannedBy, bannedIp, server, bannedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, expire, expire - System.currentTimeMillis());
	}

	public Ban(String bannedBy, String bannedIp, String server, String reason) {
		this(bannedBy, null, bannedIp, server, reason);
	}

	public Ban(String bannedBy, UUID bannedByUuid, String bannedIp, String server, String reason) {
		this(bannedBy, bannedIp, server, bannedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, -1, -1);
	}

	public Ban(String bannedBy, String bannedIp, String server, UUID bannedByUUID, long banTime, String reason, boolean unbanned, String unbannedBy, UUID unbannedByUUID, long unbanTime, long expire, long duration) {
		this.bannedBy = bannedBy;
		this.bannedIp = bannedIp;
		this.bannedByUUID = bannedByUUID;
		this.banTime = banTime;
		this.reason = reason;
		this.server = server;
		this.unbanned = unbanned;
		this.unbannedBy = unbannedBy;
		this.unbannedByUUID = unbannedByUUID;
		this.unbanTime = unbanTime;
		this.expire = expire;
		this.duration = duration;
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

	public String getServer() {
		return server;
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

	public long getUnbanTime() {
		return unbanTime;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return duration;
	}

	public boolean hasExpired() {
		return expire != -1 && expire < System.currentTimeMillis();
	}

	public boolean isPermanent() {
		return expire == -1;
	}

	public void unban() {
		this.unbanned = true;
		this.unbannedBy = "CONSOLE";
		this.unbanTime = System.currentTimeMillis();
	}

	public void unban(BattlePlayer unbanPlayer) {
		this.unbanned = true;
		this.unbannedBy = unbanPlayer.getUserName();
		this.unbannedByUUID = unbanPlayer.getUuid();
		this.unbanTime = System.currentTimeMillis();
	}

}
