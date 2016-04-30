package de.inventivegames.holograms.customEntities;

import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;

public class HologramEntitySkull extends EntityWitherSkull {

	public HologramEntitySkull(World world) {
		super(world);
		super.motX = 0.0;
		super.motY = 0.0;
		super.motZ = 0.0;
		super.dirX = 0.0;
		super.dirY = 0.0;
		super.dirZ = 0.0;
		super.boundingBox.a = 0.0;
		super.boundingBox.b = 0.0;
		super.boundingBox.c = 0.0;
		super.boundingBox.d = 0.0;
		super.boundingBox.e = 0.0;
		super.boundingBox.f = 0.0;
		a(0.0F, 0.0F);
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
		/*
		 * The field Entity.invulnerable is private. It's only used while saving
		 * NBTTags, but since the entity would be killed on chunk unload, we
		 * prefer to override isInvulnerable().
		 */
		return true;
	}

	@Override
	public int getId() {

		StackTraceElement[] elements = Thread.currentThread().getStackTrace();
		if (elements.length > 2 && elements[2] != null && elements[2].getFileName().equals("EntityTrackerEntry.java") && elements[2].getLineNumber() > 134 && elements[2].getLineNumber() < 144) {
			// Then this method is being called when creating a new packet, we
			// return a fake ID!
			return -1;
		}

		return super.getId();
	}

	@Override
	public void h() {
	}

	@Override
	public void makeSound(String sound, float f1, float f2) {
		// Remove sounds.
	}

	@Override
	public void die() {
		super.die();
	}
}
