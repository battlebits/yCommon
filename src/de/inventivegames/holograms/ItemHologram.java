package de.inventivegames.holograms;

import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import de.inventivegames.holograms.customEntities.HologramEntityItem;
import de.inventivegames.holograms.customEntities.HologramEntitySkull;
import de.inventivegames.holograms.reflection.AccessUtil;
import de.inventivegames.holograms.reflection.ClassBuilder;
import de.inventivegames.holograms.reflection.NMSClass;

public class ItemHologram extends WorldHologram {
	private ItemStack itemStack;

	protected ItemHologram(Location loc, ItemStack item) {
		super(loc, "");
		this.itemStack = item;
	}

	@Override
	protected void buildPackets(boolean rebuild) throws Exception {
		horse_1_7 = new HologramEntityItem(world);
		((HologramEntityItem) horse_1_7).setItemStackNMS(itemStack);
		((HologramEntityItem) horse_1_7).setLocation(getLocation().getX(), getLocation().getY() - 0.21, getLocation().getZ(), 0, 0);
		witherSkull_1_7 = new HologramEntitySkull(world);
		((EntityWitherSkull) witherSkull_1_7).setLocation(getLocation().getX(), getLocation().getY() - 0.21, getLocation().getZ(), 0, 0);

		this.spawnPacketWitherSkull = ClassBuilder.buildWitherSkullSpawnPacket(witherSkull_1_7);
		this.spawnPacketHorse_1_7 = new PacketPlayOutSpawnEntity((HologramEntityItem)horse_1_7, 2);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutSpawnEntity.getDeclaredField("j")).set(spawnPacketHorse_1_7, 2);
		this.hologramIDs = new int[] { ((EntityWitherSkull) witherSkull_1_7).getId(),
				//
				((Entity) horse_1_7).getId() };
		this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation(), true, false);
		this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], this.getLocation(), true, false);
		this.attachPacket = NMSClass.PacketPlayOutAttachEntity.getConstructor(int.class, NMSClass.Entity, NMSClass.Entity).newInstance(0, horse_1_7, witherSkull_1_7);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("b")).set(this.attachPacket, this.hologramIDs[1]);
		AccessUtil.setAccessible(NMSClass.PacketPlayOutAttachEntity.getDeclaredField("c")).set(this.attachPacket, this.hologramIDs[0]);
	}

	public HologramEntityItem getEntityItem() {
		return (HologramEntityItem) horse_1_7;
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
				this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, - 0.21, 0), true, false);
				this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, - 0.21, 0), true, false);
			} catch (Exception e) {
			}

			((net.minecraft.server.v1_7_R4.Entity) horse_1_7).setLocation(loc.getX(), loc.getY() - 0.21, loc.getZ(), 0, 0);
			((net.minecraft.server.v1_7_R4.Entity) witherSkull_1_7).setLocation(loc.getX(), loc.getY() - 0.21, loc.getZ(), 0, 0);
			for(Player p : this.getLocation().getWorld().getPlayers()) {
				HologramAPI.sendPacket(p, this.teleportPacketHorse_1_7);
				HologramAPI.sendPacket(p, this.teleportPacketSkull);
			}
		}

	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void addToWorld() {
		net.minecraft.server.v1_7_R4.Entity horse = (net.minecraft.server.v1_7_R4.Entity) this.horse_1_7;
		net.minecraft.server.v1_7_R4.Entity skull = (net.minecraft.server.v1_7_R4.Entity) this.witherSkull_1_7;
		world.addEntity(skull);
		world.addEntity(horse);
		for(Player p : this.getLocation().getWorld().getPlayers()) {
			HologramAPI.sendPacket(p, this.spawnPacketWitherSkull);
		}
		for(Player p : this.getLocation().getWorld().getPlayers()) {
			HologramAPI.sendPacket(p, this.teleportPacketHorse_1_7);
			HologramAPI.sendPacket(p, this.teleportPacketSkull);
		}
		((HologramEntityItem) horse).setPassengerOfNMS(skull);
		for(Player p : this.getLocation().getWorld().getPlayers()) {
			HologramAPI.sendPacket(p, this.attachPacket);
		}
	}

}
