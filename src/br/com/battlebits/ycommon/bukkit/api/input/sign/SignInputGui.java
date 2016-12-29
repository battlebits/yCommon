package br.com.battlebits.ycommon.bukkit.api.input.sign;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.common.utils.string.StringLoreUtils;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenSignEditor;
import net.minecraft.server.v1_7_R4.PacketPlayOutUpdateSign;

public class SignInputGui {

	private String[] lines;

	public SignInputGui(String str) {
		List<String> linesList = new ArrayList<>();
		linesList.add("");
		for (String s : StringLoreUtils.getLore(12, str)) {
			if (linesList.size() < 4) {
				linesList.add(s);
			}
		}
		while (linesList.size() < 4) {
			linesList.add("");
		}
		lines = new String[linesList.size()];
		lines = linesList.toArray(lines);
	}

	@SuppressWarnings("deprecation")
	public void open(Player p) {
		Location signLocation = p.getLocation().clone().subtract(0, 2, 0);
		PacketPlayOutUpdateSign updateSign = new PacketPlayOutUpdateSign(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ(), lines);
		PacketPlayOutOpenSignEditor signEditor = new PacketPlayOutOpenSignEditor(signLocation.getBlockX(), signLocation.getBlockY(), signLocation.getBlockZ());
		p.sendBlockChange(signLocation, 68, (byte) 0);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(updateSign);
		((CraftPlayer) p).getHandle().playerConnection.sendPacket(signEditor);
		p.sendBlockChange(signLocation, signLocation.getBlock().getTypeId(), signLocation.getBlock().getData());
	}

}
