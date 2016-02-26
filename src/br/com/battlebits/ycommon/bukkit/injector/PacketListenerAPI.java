package br.com.battlebits.ycommon.bukkit.injector;

import java.util.ArrayList;

public class PacketListenerAPI {

	private static ArrayList<PacketListener> listeners = new ArrayList<>();

	public static void addListener(PacketListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(PacketListener listener) {
		listeners.remove(listener);
	}

	public static ArrayList<PacketListener> getListeners() {
		return listeners;
	}

}
