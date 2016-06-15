package br.com.battlebits.ycommon.bukkit.utils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.npc.CustomPlayerAPI;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_7_R4.PacketPlayOutPosition;
import net.minecraft.server.v1_7_R4.PacketPlayOutRespawn;
import net.minecraft.server.v1_7_R4.WorldServer;

public class FakePlayerUtils {

	public static void changePlayerName(Player player, String name) {
		changePlayerName(player, name, true);
	}

	public static void changePlayerName(Player player, String name, boolean respawn) {
		Collection<? extends Player> players = BukkitMain.getPlugin().getServer().getOnlinePlayers();
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		if (respawn)
			removeFromTab(player, players);
		try {
			Field field = playerProfile.getClass().getDeclaredField("name");
			field.setAccessible(true);
			field.set(playerProfile, name);
			field.setAccessible(false);
			entityPlayer.getClass().getDeclaredField("displayName").set(entityPlayer, name);
			entityPlayer.getClass().getDeclaredField("listName").set(entityPlayer, name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (respawn)
			respawnPlayer(player, players);
	}

	public static void removePlayerSkin(Player player) {
		removePlayerSkin(player, true);
	}

	public static void removePlayerSkin(Player player, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		if (respawn) {
			respawnPlayer(player, BukkitMain.getPlugin().getServer().getOnlinePlayers());
		}
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid) {
		changePlayerSkin(player, name, uuid, true);
	}

	public static void changePlayerSkin(Player player, String name, UUID uuid, boolean respawn) {
		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		GameProfile playerProfile = entityPlayer.getProfile();
		playerProfile.getProperties().clear();
		try {
			playerProfile.getProperties().put("textures", CustomPlayerAPI.Textures.get(new GameProfile(uuid, name)));
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (respawn)
			respawnPlayer(player, BukkitMain.getPlugin().getServer().getOnlinePlayers());
	}

	public void addToTab(Player player, Collection<? extends Player> players) {
		PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutPlayerInfo updatePlayerInfo = PacketPlayOutPlayerInfo.updateDisplayName(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (!online.canSee(player))
				continue;
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(addPlayerInfo);
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(updatePlayerInfo);
		}
	}

	public static void removeFromTab(Player player, Collection<? extends Player> players) {
		PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
		for (Player online : players) {
			if (!online.canSee(player))
				continue;
			((CraftPlayer) online).getHandle().playerConnection.sendPacket(removePlayerInfo);
		}
	}

	public static void respawnPlayer(Player player, Collection<? extends Player> players) {
		respawnSelf(player);
		for (Player online : players) {
			if (online.equals(player) || !online.canSee(player)) {
				continue;
			}
			online.hidePlayer(player);
			online.showPlayer(player);
		}
	}

	@SuppressWarnings("deprecation")
	public static void respawnSelf(Player player) {

		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
		WorldServer server = ((CraftWorld) player.getWorld()).getHandle();
		PacketPlayOutPlayerInfo addPlayerInfo = PacketPlayOutPlayerInfo.addPlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutPlayerInfo removePlayerInfo = PacketPlayOutPlayerInfo.removePlayer(((CraftPlayer) player).getHandle());
		PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(server.getWorld().getEnvironment().getId(), server.difficulty, server.getWorldData().getType(), entityPlayer.playerInteractManager.getGameMode());
		PacketPlayOutPosition position = new PacketPlayOutPosition(entityPlayer.locX, entityPlayer.locY, entityPlayer.locZ, entityPlayer.yaw, entityPlayer.pitch, false);

		entityPlayer.playerConnection.sendPacket(removePlayerInfo);
		entityPlayer.playerConnection.sendPacket(addPlayerInfo);
		entityPlayer.playerConnection.sendPacket(respawn);
		entityPlayer.playerConnection.sendPacket(position);
		player.updateInventory();
	}

	public static boolean validateName(String username) {
		Pattern pattern = Pattern.compile("[a-zA-Z0-9_]{1,16}");
		Matcher matcher = pattern.matcher(username);
		return matcher.matches();
	}
}
