package br.com.battlebits.ycommon.common.friends.block;

import java.util.UUID;

public class Blocked {

	private UUID blockedUUID;
	private Long blockedTime;
	
	public Blocked(UUID blockedPlayer) {
		this.blockedTime = System.currentTimeMillis();
		this.blockedUUID = blockedPlayer;
		//CONVERT TO TIMEZONE 0
	}
	
	public UUID getPlayerUUID() {
		return blockedUUID;
	}
	
	
	public Long getBlockedTime() {
		return blockedTime;
	}
}
