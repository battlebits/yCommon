package de.inventivegames.holograms;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import de.inventivegames.holograms.customEntities.HologramEntityItem;
import de.inventivegames.holograms.customEntities.HologramEntitySkull;
import de.inventivegames.holograms.reflection.AccessUtil;
import de.inventivegames.holograms.reflection.ClassBuilder;
import de.inventivegames.holograms.reflection.NMSClass;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.WorldServer;

public class ItemPlayerHologram extends DefaultHologram {
	private Player player;
	private ItemStack itemStack;
	protected WorldServer world;
	private Object attachPacket1_8;

	protected ItemPlayerHologram(Player player, Location loc, ItemStack item) {
		super(loc, "");
		this.player = player;
		this.itemStack = item;
		world = ((CraftWorld) this.getLocation().getWorld()).getHandle();
	}

	@Override
	protected void buildPackets(boolean rebuild) throws Exception {
		horse_1_7 = new HologramEntityItem(world);
		((HologramEntityItem) horse_1_7).setItemStackNMS(itemStack);
		((HologramEntityItem) horse_1_7).setLocation(getLocation().getX(), getLocation().getY() - 0.21, getLocation().getZ(), 0, 0);

		witherSkull_1_7 = new HologramEntitySkull(world);
		((EntityWitherSkull) witherSkull_1_7).setLocation(getLocation().getX(), getLocation().getY() - 0.21, getLocation().getZ(), 0, 0);

		horse_1_8 = ClassBuilder.buildEntityHorse_1_8(world, this.getLocation().add(0, -1.41, 0), this.getText());

		this.spawnPacketWitherSkull = ClassBuilder.buildWitherSkullSpawnPacket(witherSkull_1_7);

		this.spawnPacketHorse_1_7 = new PacketPlayOutSpawnEntity((HologramEntityItem) horse_1_7, 2);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntity.getDeclaredField("j")).set(spawnPacketHorse_1_7, 2);

		this.spawnPacketHorse_1_8 = ClassBuilder.buildHorseSpawnPacket_1_8(horse_1_8, "");

		this.hologramIDs = new int[] { ((EntityWitherSkull) witherSkull_1_7).getId(),
				//
				((Entity) horse_1_7).getId(),
				//
				((Entity) horse_1_8).getId() };

		// TeleportPacket Skull
		this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, -0.21, 0), true, false);
		// TeleportPacket Item
		this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], this.getLocation().add(0, -0.21, 0), true, false);
		// TeleportPacket ArmorStand
		this.teleportPacketHorse_1_8 = ClassBuilder.buildTeleportPacket(this.hologramIDs[2], this.getLocation().add(0, -1.41, 0), true, false);

		// Metadata
		this.dataWatcherHorse_1_7 = ((HologramEntityItem) horse_1_7).getDataWatcher();

		// Attach Entity 1.7
		this.attachPacket = NMSClass.PacketPlayOutAttachEntity.getConstructor(int.class, NMSClass.Entity, NMSClass.Entity).newInstance(0, horse_1_7, witherSkull_1_7);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b")).set(this.attachPacket, this.hologramIDs[1]);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c")).set(this.attachPacket, this.hologramIDs[0]);
		// Attach Entity 1.8
		this.attachPacket1_8 = NMSClass.PacketPlayOutAttachEntity.getConstructor(int.class, NMSClass.Entity, NMSClass.Entity).newInstance(0, horse_1_7, horse_1_8);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b")).set(this.attachPacket1_8, this.hologramIDs[1]);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c")).set(this.attachPacket1_8, this.hologramIDs[2]);

		if (!rebuild) {
			this.destroyPacket = NMSClass.PacketPlayOutEntityDestroy.getConstructor(int[].class).newInstance(this.hologramIDs);
		}
	}

	@Override
	public boolean spawn() {
		this.validateDespawned();
		if (!this.packetsBuilt) {
			try {
				this.buildPackets(false);
				this.packetsBuilt = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			this.spawned = HologramAPI.spawn(this, Arrays.asList(player));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.spawned;
	}

	@Override
	public boolean despawn() {
		this.validateSpawned();
		try {
			this.spawned = !HologramAPI.despawn(this, Arrays.asList(player));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !this.spawned;
	}

	@Override
	public void setText(String text) {
		this.text = text;
		if (this.isSpawned()) {
			try {
				this.buildPackets(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.sendNamePackets(Arrays.asList(player));
		}
	}

	@Override
	public void move(@Nonnull Location loc) {
		if (loc == null) {
			throw new IllegalArgumentException("location cannot be null");
		}
		if (this.location.equals(loc)) {
			return;
		}
		if (!this.location.getWorld().equals(loc.getWorld())) {
			throw new IllegalArgumentException("cannot move to different world");
		}
		this.location = loc;
		if (this.isSpawned()) {
			try {
				// TeleportPacket Skull
				this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, -0.21, 0), true, false);
				// TeleportPacket Item
				this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], this.getLocation().add(0, -0.21, 0), true, false);
				// TeleportPacket ArmorStand
				this.teleportPacketHorse_1_8 = ClassBuilder.buildTeleportPacket(this.hologramIDs[2], this.getLocation().add(0, -1.41, 0), true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.sendTeleportPackets(Arrays.asList(player), true, true);
		}
	}

	@Override
	public Hologram addLineBelow(String text) {
		return null;
	}

	@Override
	public Hologram addLineAbove(String text) {
		return null;
	}

	@Override
	protected void sendSpawnPackets(final Collection<? extends Player> receivers, final boolean holo, final boolean touch) {
		if (holo) {
			for (Player p : receivers) {
				HologramAPI.sendPacket(p, this.spawnPacketHorse_1_7);
				if (HologramAPI.getVersion(p) > 5) {
					HologramAPI.sendPacket(p, this.spawnPacketHorse_1_8);
					HologramAPI.sendPacket(p, this.attachPacket1_8);
				} else {
					HologramAPI.sendPacket(p, this.spawnPacketWitherSkull);
					HologramAPI.sendPacket(p, this.attachPacket);
				}
			}
		}
		if (holo || touch) {
			new BukkitRunnable() {

				@Override
				public void run() {
					ItemPlayerHologram.this.sendTeleportPackets(receivers, holo, touch);
				}
			}.runTaskLaterAsynchronously(BukkitMain.getPlugin(), 1L);
		}
	}

	@Override
	protected void sendNamePackets(final Collection<? extends Player> receivers) {
		for (Player p : receivers) {
			try {
				int id = this.hologramIDs[1];
				Object dataWatcher = this.dataWatcherHorse_1_7;
				Object packet = ClassBuilder.buildNameMetadataPacket(id, dataWatcher, 2, 3, "");
				HologramAPI.sendPacket(p, packet);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void sendTeleportPackets(final Collection<? extends Player> receivers, boolean holo, boolean touch) {
		for (Player p : receivers) {
			if (HologramAPI.getVersion(p) > 5) {
				HologramAPI.sendPacket(p, this.teleportPacketHorse_1_8);
			} else {
				HologramAPI.sendPacket(p, this.teleportPacketSkull);
			}
		}
	}

	@Override
	protected void sendAttachPacket(Collection<? extends Player> receivers) {

	}

}
