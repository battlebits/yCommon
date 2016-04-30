package de.inventivegames.holograms;

import java.util.Arrays;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RunningHologram extends DefaultHologram {
	private Player player;
	private double addOffset;

	protected RunningHologram(Player player, String text) {
		this(player, text, 0);
	}

	protected RunningHologram(Player player, String text, double addOffset) {
		super(player.getLocation().add(0, addOffset, 0), text);
		this.player = player;
		this.addOffset = addOffset;
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
				this.buildPackets(true);// Re-build the packets since the
										// location is now different
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.sendTeleportPackets(Arrays.asList(player), true, true);
		}
	}

	@Override
	public Hologram addLineBelow(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createRunningHologram(player, text, addOffset - 0.25);
		this.lineBelow = hologram;
		((DefaultHologram) hologram).lineAbove = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public Hologram addLineAbove(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createRunningHologram(player, text, addOffset + 0.25);
		this.lineAbove = hologram;
		((DefaultHologram) hologram).lineBelow = this;

		hologram.spawn();
		return hologram;
	}

	public void run() {
		Location loc = player.getLocation().add(0, 2 + addOffset, 0).add(player.getEyeLocation().getDirection().multiply(4));
		move(loc);
	}

}
