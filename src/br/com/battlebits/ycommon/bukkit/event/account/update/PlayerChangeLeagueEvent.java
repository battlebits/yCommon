package br.com.battlebits.ycommon.bukkit.event.account.update;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;
import br.com.battlebits.ycommon.common.enums.Liga;

public class PlayerChangeLeagueEvent extends PlayerCancellableEvent {
	private BukkitPlayer bukkitPlayer;
	private Liga oldLeague;
	private Liga newLeague;

	public PlayerChangeLeagueEvent(Player p, BukkitPlayer player, Liga oldLeague, Liga newLeague) {
		super(p);
		this.bukkitPlayer = player;
		this.oldLeague = oldLeague;
		this.newLeague = newLeague;
	}

	public Liga getNewLeague() {
		return newLeague;
	}

	public Liga getOldLeague() {
		return oldLeague;
	}

	public BukkitPlayer getBukkitPlayer() {
		return bukkitPlayer;
	}
}
