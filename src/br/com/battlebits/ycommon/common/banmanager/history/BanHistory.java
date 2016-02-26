package br.com.battlebits.ycommon.common.banmanager.history;

import java.util.ArrayList;
import java.util.List;

import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;

public class BanHistory {
	private List<Ban> banHistory;
	private List<Mute> muteHistory;

	public BanHistory() {
		banHistory = new ArrayList<>();
		muteHistory = new ArrayList<>();
	}

	public List<Ban> getBanHistory() {
		return banHistory;
	}

	public List<Mute> getMuteHistory() {
		return muteHistory;
	}

}
