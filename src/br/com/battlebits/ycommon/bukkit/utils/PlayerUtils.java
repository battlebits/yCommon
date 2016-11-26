package br.com.battlebits.ycommon.bukkit.utils;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static boolean isPlayerOn18(Player p) {
		return !(((CraftPlayer) p).getHandle().playerConnection.networkManager.getVersion() < 47);
	}
}
