package br.com.battlebits.ycommon.bukkit.event.account.update;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;
import br.com.battlebits.ycommon.common.enums.Liga;

public class PlayerChangeLeagueEvent extends PlayerCancellableEvent {
	private Liga oldLeague;
	private Liga newLeague;

	public PlayerChangeLeagueEvent(Player p, Liga oldLeague, Liga newLeague) {
		super(p);
		this.oldLeague = oldLeague;
		this.newLeague = newLeague;
	}

	public Liga getNewLeague() {
		return newLeague;
	}

	public Liga getOldLeague() {
		return oldLeague;
	}
}
