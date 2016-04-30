package de.inventivegames.holograms;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import de.inventivegames.holograms.reflection.ClassBuilder;

public class PlayerHologram extends DefaultHologram {
	private Player player;

	protected PlayerHologram(Player player, Location loc, String text) {
		super(loc, text);
		this.player = player;
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
				this.teleportPacketSkull = ClassBuilder.buildTeleportPacket(this.hologramIDs[0], this.getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
				this.teleportPacketHorse_1_7 = ClassBuilder.buildTeleportPacket(this.hologramIDs[1], this.getLocation().add(0, HologramOffsets.WITHER_SKULL_HORSE, 0), true, false);
				this.teleportPacketHorse_1_8 = ClassBuilder.buildTeleportPacket(this.hologramIDs[2], this.getLocation().add(0, HologramOffsets.ARMOR_STAND_PACKET, 0), true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.sendTeleportPackets(Arrays.asList(player), true, true);
		}
	}

	@Override
	public Hologram addLineBelow(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createPlayerHologram(player, getLocation().add(0, -.25, 0), text);
		this.lineBelow = hologram;
		((DefaultHologram) hologram).lineAbove = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public Hologram addLineAbove(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createPlayerHologram(player, getLocation().add(0, +.25, 0), text);
		this.lineAbove = hologram;
		((DefaultHologram) hologram).lineBelow = this;

		hologram.spawn();
		return hologram;
	}

}
