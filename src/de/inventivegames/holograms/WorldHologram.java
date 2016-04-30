package de.inventivegames.holograms;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import de.inventivegames.holograms.customEntities.HologramEntityHorse;
import de.inventivegames.holograms.customEntities.HologramEntitySkull;
import de.inventivegames.holograms.reflection.AccessUtil;
import de.inventivegames.holograms.reflection.ClassBuilder;
import de.inventivegames.holograms.reflection.NMSClass;
import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WorldServer;

public class WorldHologram extends DefaultHologram {

	protected WorldServer world;

	protected WorldHologram(Location loc, String text) {
		super(loc, text);
		world = ((CraftWorld) this.getLocation().getWorld()).getHandle();
	}

	@Override
	protected void buildPackets(boolean rebuild) throws Exception {
		horse_1_7 = new HologramEntityHorse(world);
		((EntityHorse) horse_1_7).setLocation(getLocation().getX(), getLocation().getY() + HologramOffsets.WITHER_SKULL_HORSE, getLocation().getZ(), 0, 0);
		((EntityHorse) horse_1_7).setCustomNameVisible(true);
		((EntityHorse) horse_1_7).setCustomName(getText());
		((EntityHorse) horse_1_7).setAge(-1700000);
		witherSkull_1_7 = new HologramEntitySkull(world);
		((EntityWitherSkull) witherSkull_1_7).setLocation(getLocation().getX(), getLocation().getY() + HologramOffsets.WITHER_SKULL_HORSE, getLocation().getZ(), 0, 0);
		this.spawnPacketWitherSkull = ClassBuilder.buildWitherSkullSpawnPacket(witherSkull_1_7);
		this.spawnPacketHorse_1_7 = new PacketPlayOutSpawnEntityLiving((EntityHorse) horse_1_7);
		
		this.hologramIDs = new int[] { ((EntityWitherSkull) witherSkull_1_7).getId(),
				//
				((EntityHorse) horse_1_7).getId() };
		
		this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
		this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], this.getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
		this.attachPacket = NMSClass.PacketPlayOutAttachEntity.getConstructor(int.class, NMSClass.Entity, NMSClass.Entity).newInstance(0, horse_1_7, witherSkull_1_7);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b")).set(this.attachPacket, this.hologramIDs[1]);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c")).set(this.attachPacket, this.hologramIDs[0]);
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
			this.spawned = HologramAPI.spawnWorld(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.spawned;
	}

	@Override
	public boolean despawn() {
		world.removeEntity((net.minecraft.server.v1_7_R4.Entity) witherSkull_1_7);
		world.removeEntity((net.minecraft.server.v1_7_R4.Entity) horse_1_7);
		return false;
	}

	@Override
	public void setText(String text) {
		this.text = text;
		((EntityHorse) horse_1_7).setCustomName(text);
	}

	@Override
	public void move(Location loc) {
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
				this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
				this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
			} catch (Exception e) {

			}
			((net.minecraft.server.v1_7_R4.Entity) horse_1_7).setLocation(loc.getX(), loc.getY() + HologramOffsets.WITHER_SKULL_HORSE, loc.getZ(), 0, 0);
			((net.minecraft.server.v1_7_R4.Entity) witherSkull_1_7).setLocation(loc.getX(), loc.getY() + HologramOffsets.WITHER_SKULL_HORSE, loc.getZ(), 0, 0);
			for (Player p : this.getLocation().getWorld().getPlayers()) {
				HologramAPI.sendPacket(p, this.teleportPacketHorse_1_7);
				HologramAPI.sendPacket(p, this.teleportPacketSkull);
			}
		}

	}

	@Override
	public Hologram addLineBelow(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createWorldHologram(this.getLocation().subtract(0, 0.25, 0), text);
		this.lineBelow = hologram;
		((WorldHologram) hologram).lineAbove = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public Hologram addLineAbove(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createWorldHologram(this.getLocation().add(0, 0.25, 0), text);
		this.lineAbove = hologram;
		((DefaultHologram) hologram).lineBelow = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public void setAttachedTo(Entity attachedTo) {
		if (attachedTo == this.attachedTo) {
			return;
		}
		this.attachedTo = attachedTo;
		if (this.isSpawned()) {
			if (getAttachedTo() == null) {
				((net.minecraft.server.v1_7_R4.Entity) witherSkull_1_7).setPassengerOf(null);
			} else {
				net.minecraft.server.v1_7_R4.Entity entity = ((CraftEntity) attachedTo).getHandle();
				((net.minecraft.server.v1_7_R4.Entity) witherSkull_1_7).setPassengerOf(entity);
			}
		}
	}

	public void addToWorld() {
		net.minecraft.server.v1_7_R4.Entity horse = (net.minecraft.server.v1_7_R4.Entity) this.horse_1_7;
		net.minecraft.server.v1_7_R4.Entity skull = (net.minecraft.server.v1_7_R4.Entity) this.witherSkull_1_7;
		world.addEntity(skull);
		world.addEntity(horse);
		horse.setPassengerOf(skull);
	}

}
