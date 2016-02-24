package br.com.battlebits.ycommon.common.banmanager.history;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;

public class BanHistory {
	private UUID uuid;
	private List<Ban> banHistory;
	private List<Mute> muteHistory;

	public BanHistory(UUID uuid) {
		this.uuid = uuid;
		banHistory = new ArrayList<>();
		muteHistory = new ArrayList<>();
	}

	public UUID getUuid() {
		return uuid;
	}

	public List<Ban> getBanHistory() {
		return banHistory;
	}

	public List<Mute> getMuteHistory() {
		return muteHistory;
	}

}
