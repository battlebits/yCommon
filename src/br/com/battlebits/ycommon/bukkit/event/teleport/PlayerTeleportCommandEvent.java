package br.com.battlebits.ycommon.bukkit.event.teleport;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;

public class PlayerTeleportCommandEvent extends PlayerCancellableEvent {

	private TeleportResult result;

	public PlayerTeleportCommandEvent(Player player, TeleportResult result) {
		super(player);
		this.result = result;
	}

	public TeleportResult getResult() {
		return result;
	}
	
	public void setResult(TeleportResult result) {
		this.result = result;
	}

	public static enum TeleportResult {
		NO_PERMISSION, ONLY_PLAYER_TELEPORT, ALLOWED;
	}

}
