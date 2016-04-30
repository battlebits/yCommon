/*
 * Copyright 2015 Marvin Schäfer (inventivetalent). All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice, this list of
 *       conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright notice, this list
 *       of conditions and the following disclaimer in the documentation and/or other materials
 *       provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package de.inventivegames.holograms;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.server.v1_7_R4.EntityTypes;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.inventivegames.holograms.customEntities.HologramEntityHorse;
import de.inventivegames.holograms.customEntities.HologramEntityItem;
import de.inventivegames.holograms.customEntities.HologramEntitySkull;
import de.inventivegames.holograms.reflection.AccessUtil;
import de.inventivegames.holograms.reflection.NMUClass;
import de.inventivegames.holograms.reflection.Reflection;

public abstract class HologramAPI {

	protected static boolean is1_8 = false;
	protected static boolean packetsEnabled = false;
	protected static boolean useProtocolSupport = false;

	protected static final ArrayList<Hologram> holograms = new ArrayList<>();
	// Protocol Support
	static Class<?> ProtocolSupportAPI;
	static Class<?> ProtocolVersion;

	static Method ProtocolSupportAPI_getProtocolVersion;
	static Method ProtocolVersion_getId;
	static boolean registred;

	static {
		is1_8 = Reflection.getVersion().contains("1_8");
		try {
			registerCustomEntity(HologramEntityHorse.class, "EntityHorse", 100);
			registerCustomEntity(HologramEntitySkull.class, "WitherSkull", 19);
			registerCustomEntity(HologramEntityItem.class, "Item", 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Hologram createHologram(Location loc, String text) {
		Hologram hologram = new DefaultHologram(loc, text);
		holograms.add(hologram);
		return hologram;
	}

	public static Hologram createRunningHologram(Player player, String text, double addOffSet) {
		Hologram hologram = new RunningHologram(player, text, addOffSet);
		holograms.add(hologram);

		return hologram;
	}

	public static Hologram createWorldItemHologram(Location loc, ItemStack item) {
		Hologram hologram = new ItemHologram(loc, item);
		holograms.add(hologram);
		return hologram;
	}

	public static Hologram createPlayerItemHologram(Player p, Location loc, ItemStack item) {
		Hologram hologram = new ItemPlayerHologram(p, loc, item);
		holograms.add(hologram);
		return hologram;
	}

	public static Hologram createPlayerHologram(Player p, Location loc, String text) {
		Hologram hologram = new PlayerHologram(p, loc, text);
		holograms.add(hologram);
		return hologram;
	}

	public static Hologram createWorldHologram(Location loc, String text) {
		Hologram hologram = new WorldHologram(loc, text);
		holograms.add(hologram);
		return hologram;
	}

	public static boolean removeHologram(Location loc, String text) {
		Hologram toRemove = null;
		for (Hologram h : holograms) {
			if (h.getLocation().equals(loc) && h.getText().equals(text)) {
				toRemove = h;
				break;
			}
		}
		if (toRemove != null) {
			return removeHologram(toRemove);
		}
		return false;
	}

	public static boolean removeHologram(@Nonnull Hologram hologram) {
		if (hologram.isSpawned()) {
			hologram.despawn();
		}
		return holograms.remove(hologram);
	}

	public static Collection<Hologram> getHolograms() {
		return new ArrayList<>(holograms);
	}

	protected static boolean spawn(@Nonnull final Hologram hologram, final Collection<? extends Player> receivers) throws Exception {
		if (hologram == null) {
			throw new IllegalArgumentException("hologram cannot be null");
		}
		if (receivers.isEmpty()) {
			return false;
		}

		((CraftHologram) hologram).sendSpawnPackets(receivers, true, true);
		((CraftHologram) hologram).sendTeleportPackets(receivers, true, true);
		((CraftHologram) hologram).sendNamePackets(receivers);
		((CraftHologram) hologram).sendAttachPacket(receivers);
		return true;
	}

	protected static boolean spawnWorld(@Nonnull final Hologram hologram) throws Exception {
		if (hologram == null) {
			throw new IllegalArgumentException("hologram cannot be null");
		}
		((WorldHologram) hologram).addToWorld();
		return true;
	}

	protected static boolean despawn(@Nonnull Hologram hologram, Collection<? extends Player> receivers) throws Exception {
		if (hologram == null) {
			throw new IllegalArgumentException("hologram cannot be null");
		}
		if (receivers.isEmpty()) {
			return false;
		}

		((CraftHologram) hologram).sendDestroyPackets(receivers);
		return true;
	}

	protected static void sendPacket(Player p, Object packet) {
		if (p == null || packet == null) {
			throw new IllegalArgumentException("player and packet cannot be null");
		}
		try {
			Object handle = Reflection.getHandle(p);
			Object connection = Reflection.getFieldWithException(handle.getClass(), "playerConnection").get(handle);
			Reflection.getMethod(connection.getClass(), "sendPacket", Reflection.getNMSClass("Packet")).invoke(connection, new Object[] { packet });
		} catch (Exception e) {
		}
	}

	protected static int getVersion(Player p) {
		try {
			if (useProtocolSupport) {
				Object protocolVersion = ProtocolSupportAPI_getProtocolVersion.invoke(null, p);
				int id = (int) ProtocolVersion_getId.invoke(protocolVersion);
				return id;
			} else {
				final Object handle = Reflection.getHandle(p);
				Object connection = AccessUtil.setAccessible(handle.getClass().getDeclaredField("playerConnection")).get(handle);
				Object network = AccessUtil.setAccessible(connection.getClass().getDeclaredField("networkManager")).get(connection);
				String name = null;
				if (HologramAPI.is1_8) {
					if (Reflection.getVersion().contains("1_8_R1")) {
						name = "i";
					}
					if (Reflection.getVersion().contains("1_8_R2")) {
						name = "k";
					}
				} else {
					name = "m";
				}
				if (name == null) {
					// System.err.println("Invalid server version! Unable to find proper channel-field in getVersion.");
					return 99;
				}
				Object channel = AccessUtil.setAccessible(network.getClass().getDeclaredField(name)).get(network);

				Object version = 0;
				try {
					version = AccessUtil.setAccessible(network.getClass().getDeclaredMethod("getVersion", NMUClass.io_netty_channel_Channel)).invoke(network, channel);
				} catch (Exception e) {
					// e.printStackTrace();
					return 182;
				}
				return (int) version;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return 0;
	}

	public static boolean is1_8() {
		return is1_8;
	}

	public static boolean packetsEnabled() {
		return packetsEnabled;
	}

	protected static void enableProtocolSupport() {
		useProtocolSupport = true;

		try {
			ProtocolSupportAPI = Class.forName("protocolsupport.api.ProtocolSupportAPI");
			ProtocolVersion = Class.forName("protocolsupport.api.ProtocolVersion");

			ProtocolSupportAPI_getProtocolVersion = Reflection.getMethod(ProtocolSupportAPI, "getProtocolVersion", new Class[] { Player.class });
			ProtocolVersion_getId = Reflection.getMethod(ProtocolVersion, "getId");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean isHologramEntity(int entityId) {
		for (Hologram holo : (ArrayList<Hologram>) holograms.clone()) {
			if (holo instanceof CraftHologram) {
				if (((CraftHologram) holo).matchesHologramID(entityId)) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static void registerCustomEntity(Class entityClass, String name, int id) throws Exception {
		// Normal entity registration.
		putInPrivateStaticMap(EntityTypes.class, "d", entityClass, name);
		putInPrivateStaticMap(EntityTypes.class, "f", entityClass, Integer.valueOf(id));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void putInPrivateStaticMap(Class<?> clazz, String fieldName, Object key, Object value) throws Exception {
		Field field = clazz.getDeclaredField(fieldName);
		field.setAccessible(true);
		Map map = (Map) field.get(null);
		map.put(key, value);
	}
}
