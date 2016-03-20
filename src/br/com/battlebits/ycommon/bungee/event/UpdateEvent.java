package br.com.battlebits.ycommon.bungee.event;

import net.md_5.bungee.api.plugin.Event;

public class UpdateEvent extends Event {

	private UpdateType type;

	public UpdateEvent(UpdateType type) {
		this.type = type;
	}

	public UpdateType getType() {
		return type;
	}

	public static enum UpdateType {
		TICK, SECOND, MINUTE;
	}
}
