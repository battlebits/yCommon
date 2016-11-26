package br.com.battlebits.ycommon.bukkit.api.input.sign;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.injector.PacketListenerAPI;

public class SignInputManager {

	private HashMap<UUID, SignInputHandler> uuidInputHandler;
	private SignInputPacketListener packetListener;
	private SignInputListener listener;

	public SignInputManager() {
		uuidInputHandler = new HashMap<>();
		packetListener = new SignInputPacketListener(this);
		listener = new SignInputListener(this);
		PacketListenerAPI.addListener(packetListener);
		Bukkit.getPluginManager().registerEvents(listener, BukkitMain.getPlugin());
	}

	public void stop() {
		uuidInputHandler.clear();
		PacketListenerAPI.removeListener(packetListener);
		HandlerList.unregisterAll(listener);
		uuidInputHandler = null;
		packetListener = null;
		listener = null;
	}

	public void open(SignInputGui gui, Player p, SignInputHandler handler) {
		tryToRemove(p.getUniqueId());
		uuidInputHandler.put(p.getUniqueId(), handler);
		gui.open(p);
	}

	public void tryToRemove(UUID id) {
		uuidInputHandler.remove(id);
	}

	public boolean hasSign(UUID id) {
		return uuidInputHandler.containsKey(id);
	}

	public boolean handle(Player p, String[] lines) {
		if (hasSign(p.getUniqueId())) {
			uuidInputHandler.get(p.getUniqueId()).onDone(p, lines);
			uuidInputHandler.remove(p.getUniqueId());
			return true;
		}
		return false;
	}

}
