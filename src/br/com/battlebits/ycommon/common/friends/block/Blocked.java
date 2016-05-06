package br.com.battlebits.ycommon.common.friends.block;

import java.util.UUID;

import br.com.battlebits.ycommon.common.time.TimeZone;
import br.com.battlebits.ycommon.common.time.TimeZoneConversor;

public class Blocked {

	private UUID blockedUUID;
	private Long blockedTime;

	public Blocked(UUID blockedPlayer) {
		this.blockedTime = TimeZoneConversor.getCurrentMillsTimeIn(TimeZone.GMT0);
		this.blockedUUID = blockedPlayer;
	}

	public UUID getPlayerUUID() {
		return blockedUUID;
	}

	public Long getBlockedTime() {
		return blockedTime;
	}
}
