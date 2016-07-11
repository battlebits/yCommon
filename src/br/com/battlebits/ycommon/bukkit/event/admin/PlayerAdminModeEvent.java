package br.com.battlebits.ycommon.bukkit.event.admin;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;

public class PlayerAdminModeEvent extends PlayerCancellableEvent {

	private AdminMode adminMode;

	public PlayerAdminModeEvent(Player player, AdminMode adminMode) {
		super(player);
		this.adminMode = adminMode;
	}

	public AdminMode getAdminMode() {
		return adminMode;
	}

	public static enum AdminMode {
		ADMIN, //
		PLAYER
	}

}
