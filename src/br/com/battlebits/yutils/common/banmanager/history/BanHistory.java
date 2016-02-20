package br.com.battlebits.yutils.common.banmanager.history;

import java.util.List;
import java.util.UUID;

import br.com.battlebits.yutils.common.banmanager.constructors.Ban;
import br.com.battlebits.yutils.common.banmanager.constructors.Mute;

public class BanHistory {
	private UUID uuid;
	private List<Ban> banHistory;
	private List<Mute> muteHistory;
}
