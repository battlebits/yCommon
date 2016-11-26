package br.com.battlebits.ycommon.bukkit.api.input;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.api.input.anvil.AnvilInputManager;
import br.com.battlebits.ycommon.bukkit.api.input.sign.SignInputManager;
import br.com.battlebits.ycommon.bukkit.utils.PlayerUtils;

public class InputManager {

	private static AnvilInputManager anvilInputManager = new AnvilInputManager();
	private static SignInputManager signInputManager = new SignInputManager();

	public void stop() {
		anvilInputManager.stop();
	}

	public static void open(Player p, InputGui gui) {
		p.closeInventory();
		if (PlayerUtils.isPlayerOn18(p)) {
			signInputManager.open(gui.getSignGui(), p, gui.getSignHandler());
		} else {
			anvilInputManager.openAnvilSearchGui(gui.getAnvil(p));
		}
	}

	public static AnvilInputManager getAnvilSearchManager() {
		return anvilInputManager;
	}

	public static SignInputManager getSignSearchManager() {
		return signInputManager;
	}

}
