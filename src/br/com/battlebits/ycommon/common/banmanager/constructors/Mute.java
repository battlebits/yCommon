package br.com.battlebits.ycommon.common.banmanager.constructors;

import java.util.UUID;

public class Mute {

	private UUID mutedPlayer;
	private String mutedBy;
	private String mutedIp;

	private UUID mutedByUUID;
	private long muteTime;
	private String reason;

	private boolean unmuted;
	private String unmutedBy;
	private UUID mutedUUID;

	private long expire;
	private long duration;

	public Mute(UUID mutedPlayer, String mutedBy, String mutedIp, UUID mutedByUUID, long muteTime, String reason, boolean unmuted, String unmutedBy, UUID mutedUUID, long expire, long duration) {
		this.mutedPlayer = mutedPlayer;
		this.mutedBy = mutedBy;
		this.mutedIp = mutedIp;
		this.mutedByUUID = mutedByUUID;
		this.muteTime = muteTime;
		this.reason = reason;
		this.unmuted = unmuted;
		this.unmutedBy = unmutedBy;
		this.mutedUUID = mutedUUID;
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

	public boolean isUnmuted() {
		return unmuted;
	}

	public String getUnmutedBy() {
		return unmutedBy;
	}

	public UUID getMutedUUID() {
		return mutedUUID;
	}

	public long getExpire() {
		return expire;
	}

	public long getDuration() {
		return duration;
	}

	public boolean hasExpired() {
		return !isPermanent() && expire < System.currentTimeMillis();
	}

	public boolean isPermanent() {
		return expire == -1;
	}

}
