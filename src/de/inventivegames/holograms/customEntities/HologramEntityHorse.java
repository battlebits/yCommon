package de.inventivegames.holograms.customEntities;

import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;

public class HologramEntityHorse extends EntityHorse {

	public HologramEntityHorse(World world) {
		super(world);
		super.ageLocked = true;
		super.persistent = true;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		a(0.0F, 0.0F);
		setAge(-1700000); // This is a magic value. No one will see the real
							// horse.
	}

	@Override
	public void h() {
		// Checks every 20 ticks.
		if (ticksLived % 20 == 0) {
			// The horse dies without a vehicle.
			if (this.vehicle == null) {
				die();
			}
		}
	}

	@Override
	public void b(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}

	@Override
	public boolean c(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public boolean d(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
		return false;
	}

	@Override
	public void e(NBTTagCompound nbttagcompound) {
		// Do not save NBT.
	}

	@Override
	public boolean isInvulnerable() {
		return true;
	}

	@Override
	public void makeSound(String sound, float volume, float pitch) {
		// Remove sounds.
	}

	@Override
	public void die() {
		super.die();
	}
}
