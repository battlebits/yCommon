package br.com.battlebits.ycommon.bukkit.api.input.anvil;

import org.bukkit.entity.Player;

public interface AnvilInputHandler {

	public void onDone(Player p, String name);

	public void onClose(Player p);

}
