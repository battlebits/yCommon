package br.com.battlebits.ycommon.bukkit.api.input.sign;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

class SignInputListener implements Listener {

	private SignInputManager manager;

	public SignInputListener(SignInputManager searchManager) {
		this.manager = searchManager;
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		manager.tryToRemove(e.getPlayer().getUniqueId());
	}

}
