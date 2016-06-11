package br.com.battlebits.ycommon.bukkit.api.actionbar;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class ActionBar {

	public static void sendActionChatPacket(Player player, String text) {
		if (((CraftPlayer) player).getHandle().playerConnection.networkManager.getVersion() < 47) {
			return;
		}
		IChatBaseComponent comp = ChatSerializer.a("{\"text\":\"" + text + " \"}");
		PacketPlayOutChat bar = new PacketPlayOutChat(comp, 2);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(bar);
	}

}
