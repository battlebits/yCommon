package br.com.battlebits.ycommon.bukkit.injector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.injector.PacketListener.PacketObject;
import br.com.battlebits.ycommon.common.utils.reflection.ReflectionUtils;
import de.inventivegames.holograms.HologramAPI;
import de.inventivegames.holograms.HologramOffsets;
import de.inventivegames.holograms.reflection.AccessUtil;
import de.inventivegames.holograms.reflection.NMSClass;
import de.inventivegames.holograms.reflection.NMUClass;
import io.netty.channel.Channel;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.NetworkManager;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;

public class Injector {
	public static void createTinyProtocol(final Plugin plugin) {
		new TinyProtocol(plugin) {
			@Override
			public Object onPacketOutAsync(final Player reciever, Channel channel, Object packet) {
				if (!(packet instanceof Packet))
					return super.onPacketOutAsync(reciever, channel, packet);
				if (channel == null) {
					return super.onPacketOutAsync(reciever, channel, packet);
				}
				PacketObject object = new PacketObject(reciever, channel, (Packet) packet);
				@SuppressWarnings("unchecked")
				Iterator<PacketListener> iterator = ((List<PacketListener>) PacketListenerAPI.getListeners().clone()).iterator();
				while (iterator.hasNext()) {
					iterator.next().onPacketSend(object);
				}
				if (object.isCancelled())
					return null;
				return object.getPacket();
			}

			@SuppressWarnings("unchecked")
			@Override
			public Object onPacketInAsync(Player sender, Channel channel, Object packet) {
				if (!(packet instanceof Packet))
					return super.onPacketInAsync(sender, channel, packet);
				if (channel == null) {
					return super.onPacketInAsync(sender, channel, packet);
				}
				PacketObject object = new PacketObject(sender, channel, (Packet) packet);
				Iterator<PacketListener> iterator = ((List<PacketListener>) PacketListenerAPI.getListeners().clone()).iterator();
				while (iterator.hasNext()) {
					iterator.next().onPacketReceive(object);
				}
				if (object.isCancelled())
					return null;
				return object.getPacket();
			}
		};

		PacketListenerAPI.addListener(new PacketListener() {

			@Override
			public void onPacketSend(PacketObject object) {
				final Player reciever = object.getPlayer();
				Channel channel = object.getChannel();
				Packet packet = object.getPacket();
				if (channel.attr(NetworkManager.protocolVersion).get() != 47)
					return;
				try {
					if (packet instanceof PacketPlayOutSpawnEntityLiving) {
						PacketPlayOutSpawnEntityLiving living = (PacketPlayOutSpawnEntityLiving) packet;

						int id = (int) ReflectionUtils.getPrivateFieldObject(living, "a");

						if (!HologramAPI.isHologramEntity(id)) {
							return;
						}
						net.minecraft.server.v1_7_R4.Entity e = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id);
						if (e == null) {
							return;
						}
						Entity entity = e.getBukkitEntity();

						if (entity.getType() == EntityType.HORSE) {

							ReflectionUtils.setPrivateFieldObject(living, "b", 30);
							DataWatcher watcher = (DataWatcher) ReflectionUtils.getPrivateFieldObject(living, "l");

							if (watcher != null) {
								DataWatcher newWatcher = new DataWatcher(e);
								fixIndexesAndCopy(watcher, newWatcher);
								// cloneWatcher(watcher, newWatcher);
								// fixIndexes(newWatcher);
								AccessUtil.setAccessible(PacketPlayOutSpawnEntityLiving.class.getDeclaredField("l")).set(living, newWatcher);
							}

							return;
						}
					} else if (packet instanceof PacketPlayOutSpawnEntity) {

						PacketPlayOutSpawnEntity spawn = (PacketPlayOutSpawnEntity) packet;

						final int id = (int) ReflectionUtils.getPrivateFieldObject(spawn, "a");

						if (!HologramAPI.isHologramEntity(id)) {
							return;
						}
						net.minecraft.server.v1_7_R4.Entity e = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id);
						if (e == null) {
							return;
						}
						Entity entity = e.getBukkitEntity();

						if (entity.getType() == EntityType.WITHER_SKULL) {
							ReflectionUtils.setPrivateFieldObject(spawn, "j", 78);

							final DataWatcher watcher = new DataWatcher(((CraftWorld) reciever.getWorld()).getHandle().getEntity(id));
							Object map_1_8 = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues")).get(watcher);
							NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(map_1_8, 0, NMSClass.WatchableObject.getConstructor(int.class, int.class, Object.class).newInstance(0, 0, (byte) 32));

							new BukkitRunnable() {

								@Override
								public void run() {
									PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(id, watcher, true);
									((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(metadata);
								}
							}.runTaskLater(plugin, 5);

							return;
						}
					} else if (packet instanceof PacketPlayOutEntityMetadata) {

						PacketPlayOutEntityMetadata metadata = (PacketPlayOutEntityMetadata) packet;

						int id = (int) ReflectionUtils.getPrivateFieldObject(metadata, "a");
						if (!HologramAPI.isHologramEntity(id))
							return;

						net.minecraft.server.v1_7_R4.Entity en = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id);
						if (en == null) {
							return;
						}
						CraftEntity e = en.getBukkitEntity();
						if (e.getType() == EntityType.WITHER_SKULL) {

							final DataWatcher watcher = new DataWatcher(((CraftWorld) reciever.getWorld()).getHandle().getEntity(id));
							Object map_1_8 = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues")).get(watcher);
							NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(map_1_8, 0, NMSClass.WatchableObject.getConstructor(int.class, int.class, Object.class).newInstance(0, 0, (byte) 32));
							object.setPacket(new PacketPlayOutEntityMetadata(id, watcher, true));

							return;
						}
						if (e.getType() != EntityType.HORSE)
							return;
						DataWatcher watcher = e.getHandle().getDataWatcher();

						if (watcher != null) {
							DataWatcher newWatcher = new DataWatcher(e.getHandle());
							fixIndexesAndCopy(watcher, newWatcher);
							// cloneWatcher(watcher, newWatcher);
							// fixIndexes(newWatcher);
							ReflectionUtils.setPrivateFieldObject(metadata, "b", newWatcher.c());

							return;
						}
					} else if (packet instanceof PacketPlayOutAttachEntity) {

						PacketPlayOutAttachEntity attach = (PacketPlayOutAttachEntity) packet;

						int id1 = (int) ReflectionUtils.getPrivateFieldObject(attach, "b");
						int id2 = (int) ReflectionUtils.getPrivateFieldObject(attach, "c");

						if (id2 == -1 || !HologramAPI.isHologramEntity(id2)) {
							return;
						}
						net.minecraft.server.v1_7_R4.Entity en1 = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id1);
						if (en1 == null) {
							return;
						}
						net.minecraft.server.v1_7_R4.Entity en2 = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id2);
						if (en2 == null) {
							return;
						}
						Entity passenger = en1.getBukkitEntity();
						Entity vehicle = en2.getBukkitEntity();

						if (vehicle != null && passenger != null) {
							final PacketPlayOutEntityTeleport teleport = new PacketPlayOutEntityTeleport();
							Location loc = vehicle.getLocation();
							AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("a")).set(teleport, id2);
							AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("b")).set(teleport, (int) (loc.getX() * 32D));
							AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("d")).set(teleport, (int) (loc.getZ() * 32D));
							AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("e")).set(teleport, (byte) (int) (loc.getYaw() * 256F / 360F));
							AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("f")).set(teleport, (byte) (int) (loc.getPitch() * 256F / 360F));
							if (passenger.getType() == EntityType.HORSE) {
								object.setCancelled(true);
							}
							new BukkitRunnable() {
								@Override
								public void run() {
									((CraftPlayer) reciever).getHandle().playerConnection.sendPacket(teleport);
								}
							}.runTaskLater(plugin, 1);
							return;
						}
					} else if (packet instanceof PacketPlayOutEntityTeleport) {

						PacketPlayOutEntityTeleport teleport = (PacketPlayOutEntityTeleport) packet;

						int id = (int) ReflectionUtils.getPrivateFieldObject(teleport, "a");
						if (!HologramAPI.isHologramEntity(id)) {
							return;
						}

						net.minecraft.server.v1_7_R4.Entity e = ((CraftWorld) reciever.getWorld()).getHandle().getEntity(id);
						if (e == null) {
							return;
						}
						Entity entity = e.getBukkitEntity();
						if (entity.getType() == EntityType.WITHER_SKULL) {

							Entity passenger = entity.getPassenger();

							if (passenger == null) {
								return;
							}

							if (passenger.getType() == EntityType.DROPPED_ITEM || passenger.getType() == EntityType.SLIME) {
								AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(teleport, (int) ((entity.getLocation().getY() - 1.2) * 32D));
							} else if (passenger.getType() == EntityType.HORSE) {
								AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("a")).set(teleport, entity.getPassenger().getEntityId());
								AccessUtil.setAccessible(NMSClass.PacketPlayOutEntityTeleport.getDeclaredField("c")).set(teleport, (int) ((entity.getLocation().getY() - HologramOffsets.WITHER_SKULL_HORSE + HologramOffsets.ARMOR_STAND_PACKET) * 32D));
							}
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onPacketReceive(PacketObject object) {

			}
		});
	}

	@SuppressWarnings({ "boxing" })
	private static void fixIndexesAndCopy(DataWatcher watcher, DataWatcher newDataWatcher) throws Exception {
		Object map_1_8 = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues")).get(watcher);
		Object newMap = AccessUtil.setAccessible(NMSClass.DataWatcher.getDeclaredField("dataValues")).get(newDataWatcher);
		NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(newMap, 10, NMSClass.WatchableObject.getConstructor(int.class, int.class, Object.class).newInstance(0, 10, (byte) 1));
		List<Integer> toRemove = new ArrayList<>();
		for (int i = 0; i < 100; i++) {
			Object current = NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("get", int.class).invoke(map_1_8, i);
			if (current == null) {
				continue;
			}
			int index = AccessUtil.setAccessible(NMSClass.WatchableObject.getDeclaredField("b")).getInt(current);
			NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(newMap, index, current);
			if (index == 2) {

			} else if (index != 3) {
				toRemove.add(Integer.valueOf(index));
			}
		}
		for (Integer i : toRemove) {
			NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("remove", int.class).invoke(newMap, i);
		}
		NMUClass.gnu_trove_map_hash_TIntObjectHashMap.getDeclaredMethod("put", int.class, Object.class).invoke(newMap, 0, NMSClass.WatchableObject.getConstructor(int.class, int.class, Object.class).newInstance(0, 0, (byte) 32));
	}

}
