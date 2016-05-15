package br.com.battlebits.ycommon.bukkit.event.vanish;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;

public class PlayerVisibleToPlayerEvent extends PlayerCancellableEvent {
	private Player toPlayer;

	public PlayerVisibleToPlayerEvent(Player player, Player toPlayer) {
		super(player);
		this.toPlayer = toPlayer;
	}

	public Player getToPlayer() {
		return toPlayer;
	}
}
