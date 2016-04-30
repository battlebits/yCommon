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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;

public class DefaultHologram extends CraftHologram {

	protected Location location;
	protected String text;
	private boolean touchable;

	protected boolean spawned;

	protected Entity attachedTo;

	protected Hologram lineBelow;
	protected Hologram lineAbove;
	protected Plugin rl;

	private BukkitRunnable updater;

	protected DefaultHologram(@Nonnull Location loc, String text) {
		if (loc == null) {
			throw new IllegalArgumentException("location cannot be null");
		}
		this.location = loc;
		this.text = text;
		rl = BukkitMain.getPlugin();
	}

	@Override
	public boolean isSpawned() {
		return this.spawned;
	}

	@Override
	public void spawn(long ticks) {
		if (ticks < 1) {
			throw new IllegalArgumentException("ticks must be at least 1");
		}
		this.spawn();
		new BukkitRunnable() {

			@Override
			public void run() {
				DefaultHologram.this.despawn();
			}
		}.runTaskLater(rl, ticks);
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
			this.spawned = HologramAPI.spawn(this, this.getLocation().getWorld().getPlayers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.spawned;
	}

	@Override
	public boolean despawn() {
		this.validateSpawned();
		try {
			this.spawned = !HologramAPI.despawn(this, this.getLocation().getWorld().getPlayers());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return !this.spawned;
	}

	@Override
	public void setLocation(Location loc) {
		this.move(loc);
	}

	@Override
	public Location getLocation() {
		return this.location.clone();
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
			this.sendNamePackets(this.getLocation().getWorld().getPlayers());
		}
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public void update() {
		this.setText(this.getText());
	}

	@Override
	public void update(long interval) {
		if (interval == -1) {
			if (this.updater == null) {
				throw new IllegalStateException("Not updating");
			}
			this.updater.cancel();
			this.updater = null;
			return;
		}
		if (this.updater != null) {
			throw new IllegalStateException("Already updating");
		}
		if (interval < 1) {
			throw new IllegalArgumentException("Interval must be at least 1");
		}
		this.updater = new BukkitRunnable() {

			@Override
			public void run() {
				DefaultHologram.this.update();
			}
		};
		this.updater.runTaskTimer(rl, interval, interval);
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
			this.sendTeleportPackets(this.getLocation().getWorld().getPlayers(), true, true);
		}
	}

	@Override
	public Hologram addLineBelow(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createHologram(this.getLocation().subtract(0, 0.25, 0), text);
		this.lineBelow = hologram;
		((DefaultHologram) hologram).lineAbove = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public Hologram getLineBelow() {
		this.validateSpawned();
		return this.lineBelow;
	}

	@Override
	public boolean removeLineBelow() {
		if (this.getLineBelow() != null) {
			if (this.getLineBelow().isSpawned()) {
				this.getLineBelow().despawn();
			}
			this.lineBelow = null;
			return true;
		}
		return false;
	}

	@Override
	public Collection<Hologram> getLinesBelow() {
		List<Hologram> list = new ArrayList<>();

		Hologram current = this;
		while ((current = ((DefaultHologram) current).lineBelow) != null) {
			list.add(current);
		}

		return list;
	}

	@Override
	public Hologram addLineAbove(String text) {
		this.validateSpawned();
		Hologram hologram = HologramAPI.createHologram(this.getLocation().add(0, 0.25, 0), text);
		this.lineAbove = hologram;
		((DefaultHologram) hologram).lineBelow = this;

		hologram.spawn();
		return hologram;
	}

	@Override
	public Hologram getLineAbove() {
		this.validateSpawned();
		return this.lineAbove;
	}

	@Override
	public boolean removeLineAbove() {
		if (this.getLineAbove() != null) {
			if (this.getLineAbove().isSpawned()) {
				this.getLineAbove().despawn();
			}
			this.lineAbove = null;
			return true;
		}
		return false;
	}

	@Override
	public Collection<Hologram> getLinesAbove() {
		List<Hologram> list = new ArrayList<>();

		Hologram current = this;
		while ((current = ((DefaultHologram) current).lineAbove) != null) {
			list.add(current);
		}

		return list;
	}

	@Override
	public Collection<Hologram> getLines() {
		List<Hologram> list = new ArrayList<>();
		list.addAll(this.getLinesAbove());
		list.add(this);
		list.addAll(this.getLinesBelow());
		return list;
	}

	@Override
	public void setAttachedTo(Entity attachedTo) {
		if (attachedTo == this.attachedTo) {
			return;
		}
		this.attachedTo = attachedTo;
		if (this.isSpawned()) {
			try {
				this.buildPackets(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.sendAttachPacket(this.getLocation().getWorld().getPlayers());
		}
	}

	@Override
	public Entity getAttachedTo() {
		return attachedTo;
	}

	protected void validateSpawned() {
		if (!this.spawned) {
			throw new IllegalStateException("Not spawned");
		}
	}

	protected void validateDespawned() {
		if (this.spawned) {
			throw new IllegalStateException("Already spawned");
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.location == null ? 0 : this.location.hashCode());
		result = prime * result + (this.spawned ? 1231 : 1237);
		result = prime * result + (this.text == null ? 0 : this.text.hashCode());
		result = prime * result + (this.touchable ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		DefaultHologram other = (DefaultHologram) obj;
		if (this.location == null) {
			if (other.location != null) {
				return false;
			}
		} else if (!this.location.equals(other.location)) {
			return false;
		}
		if (this.spawned != other.spawned) {
			return false;
		}
		if (this.text == null) {
			if (other.text != null) {
				return false;
			}
		} else if (!this.text.equals(other.text)) {
			return false;
		}
		if (this.touchable != other.touchable) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "{\"location\":\"" + this.location + "\",\"text\":\"" + this.text + "\",\"touchable\":\"" + this.touchable + "\",\"spawned\":\"" + this.spawned + "\",\"touchHandlers\":\"" + "" + "\"}";
	}

}
