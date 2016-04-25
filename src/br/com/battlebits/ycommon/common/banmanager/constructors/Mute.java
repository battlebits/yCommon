package br.com.battlebits.ycommon.common.banmanager.constructors;

import java.util.UUID;

import br.com.battlebits.ycommon.common.account.BattlePlayer;

public class Mute {

	private UUID mutedPlayer;
	private String mutedBy;
	private String mutedIp;

	private String server;

	private UUID mutedByUUID;
	private long muteTime;
	private String reason;

	private boolean unmuted;
	private String unmutedBy;
	private UUID unmutedByUUID;
	private long unmuteTime;

	private long expire;
	private long duration;

	public Mute(UUID mutedPlayer, String mutedBy, String mutedIp, String server, String reason, long duration) {
		this(mutedPlayer, mutedBy, null, mutedIp, server, reason, duration);
	}

	public Mute(UUID mutedPlayer, String mutedBy, UUID mutedByUuid, String mutedIp, String server, String reason, long expire) {
		this(mutedPlayer, mutedBy, mutedIp, server, mutedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, expire, expire - System.currentTimeMillis());
	}

	public Mute(UUID mutedPlayer, String mutedBy, String mutedIp, String server, String reason) {
		this(mutedPlayer, mutedBy, null, mutedIp, server, reason);
	}

	public Mute(UUID mutedPlayer, String mutedBy, UUID mutedByUuid, String mutedIp, String server, String reason) {
		this(mutedPlayer, mutedBy, mutedIp, server, mutedByUuid, System.currentTimeMillis(), reason, false, null, null, -1, -1, -1);
	}

	public Mute(UUID mutedPlayer, String mutedBy, String mutedIp, String server, UUID mutedByUUID, long muteTime, String reason, boolean unmuted, String unmutedBy, UUID unmutedByUUID, long unmuteTime, long expire, long duration) {
		this.mutedPlayer = mutedPlayer;
		this.mutedBy = mutedBy;
		this.mutedIp = mutedIp;
		this.mutedByUUID = mutedByUUID;
		this.muteTime = muteTime;
		this.reason = reason;
		this.server = server;
		this.unmuted = unmuted;
		this.unmutedBy = unmutedBy;
		this.unmutedByUUID = unmutedByUUID;
		this.unmuteTime = unmuteTime;
		this.expire = expire;
		this.duration = duration;
	}

	public UUID getMutedPlayer() {
		return mutedPlayer;
	}

	public String getMutedBy() {
		return mutedBy;
	}

	public String getMutedIp() {
		return mutedIp;
	}

	public UUID getMutedByUUID() {
		return mutedByUUID;
	}

	public long getMuteTime() {
		return muteTime;
	}

	public String getReason() {
		return reason;
	}

	public String getServer() {
		return server;
	}

	public boolean isUnmuted() {
		return unmuted;
	}

	public String getUnmutedBy() {
		return unmutedBy;
	}

	public UUID getUnmutedByUUID() {
		return unmutedByUUID;
	}

	public long getUnmuteTime() {
		return unmuteTime;
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

	public void unmute() {
		this.unmuted = true;
		this.unmutedBy = "CONSOLE";
		this.unmuteTime = System.currentTimeMillis();
	}

	public void unmute(BattlePlayer unmutePlayer) {
		this.unmuted = true;
		this.unmutedBy = unmutePlayer.getUserName();
		this.unmutedByUUID = unmutePlayer.getUuid();
		this.unmuteTime = System.currentTimeMillis();
	}

}
