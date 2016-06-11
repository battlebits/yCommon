package br.com.battlebits.ycommon.bukkit.api.title;

import java.lang.reflect.Field;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.spigotmc.ProtocolInjector.PacketTitle;
import org.spigotmc.ProtocolInjector.PacketTitle.Action;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PlayerConnection;

public class Title {
	private String title = "";
	private ChatColor titleColor = ChatColor.WHITE;
	private String subtitle = "";
	private ChatColor subtitleColor = ChatColor.WHITE;
	private int fadeInTime = -1;
	private int stayTime = -1;
	private int fadeOutTime = -1;
	private boolean ticks = false;

	public Title(String title) {
		this.title = title;
	}

	public Title(String title, String subtitle) {
		this.title = title;
		this.subtitle = subtitle;
	}

	public Title(Title title) {
		// Copy title
		this.title = title.title;
		this.subtitle = title.subtitle;
		this.titleColor = title.titleColor;
		this.subtitleColor = title.subtitleColor;
		this.fadeInTime = title.fadeInTime;
		this.fadeOutTime = title.fadeOutTime;
		this.stayTime = title.stayTime;
		this.ticks = title.ticks;
	}

	public Title(String title, String subtitle, int fadeInTime, int stayTime, int fadeOutTime) {
		this.title = title;
		this.subtitle = subtitle;
		this.fadeInTime = fadeInTime;
		this.stayTime = stayTime;
		this.fadeOutTime = fadeOutTime;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getSubtitle() {
		return this.subtitle;
	}

	public void setTitleColor(ChatColor color) {
		this.titleColor = color;
	}

	public void setSubtitleColor(ChatColor color) {
		this.subtitleColor = color;
	}

	public void setFadeInTime(int time) {
		this.fadeInTime = time;
	}

	public void setFadeOutTime(int time) {
		this.fadeOutTime = time;
	}

	public void setStayTime(int time) {
		this.stayTime = time;
	}

	public void setTimingsToTicks() {
		ticks = true;
	}

	public void setTimingsToSeconds() {
		ticks = false;
	}

	public void send(Player player) {
		// First reset previous settings
		resetTitle(player);
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47) {
			ArrayList<String> linhas = new ArrayList<>();
			if (!title.isEmpty())
				linhas.add(title);
			if (!subtitle.isEmpty())
				linhas.add(subtitle);
			String[] array = new String[linhas.size()];
			linhas.toArray(array);
			return;
		}
		PacketTitle packetTitle = new PacketTitle(Action.TITLE, fadeInTime * (ticks ? 1 : 20), stayTime * (ticks ? 1 : 20), fadeOutTime * (ticks ? 1 : 20));
		try {
			Field field = packetTitle.getClass().getField("text");
			field.setAccessible(true);
			field.set(packetTitle, ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', title) + "\",color:" + titleColor.name().toLowerCase() + "}"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (fadeInTime != -1 && fadeOutTime != -1 && stayTime != -1)
			connection.sendPacket(packetTitle);
		if (subtitle != "") {
			packetTitle = new PacketTitle(Action.SUBTITLE, ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', subtitle) + "\",color:" + subtitleColor.name().toLowerCase() + "}"));
			connection.sendPacket(packetTitle);
		}
	}

	public void broadcast() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			send(p);
		}
	}

	public void clearTitle(Player player) {
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47)
			return;
		PacketTitle packetTitle = new PacketTitle(Action.CLEAR);
		connection.sendPacket(packetTitle);
	}

	public void resetTitle(Player player) {
		EntityPlayer handle = ((CraftPlayer) player).getHandle();
		PlayerConnection connection = handle.playerConnection;
		if (connection.networkManager.getVersion() < 47)
			return;
		PacketTitle packetTitle = new PacketTitle(Action.RESET);
		connection.sendPacket(packetTitle);
	}
}
