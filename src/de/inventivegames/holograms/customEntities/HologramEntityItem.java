package de.inventivegames.holograms.customEntities;

import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityItem;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;
import net.minecraft.server.v1_7_R4.NBTTagString;
import net.minecraft.server.v1_7_R4.World;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;

import de.inventivegames.holograms.reflection.AccessUtil;

public class HologramEntityItem extends EntityItem {
	private boolean lockTick;

	public HologramEntityItem(World world) {
		super(world);
		this.pickupDelay = 2147483647;
	}

	public void h() {
		this.ticksLived = 0;
	}

	public net.minecraft.server.v1_7_R4.ItemStack getItemStack() {
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		if ((stacktrace.length > 2) && (stacktrace[2].getClassName().contains("EntityInsentient"))) {
			return null;
		}
		return super.getItemStack();
	}

	public void b(NBTTagCompound nbttagcompound) {
	}

	public boolean c(NBTTagCompound nbttagcompound) {
		return false;
	}

	public boolean d(NBTTagCompound nbttagcompound) {
		return false;
	}

	public void e(NBTTagCompound nbttagcompound) {
	}

	public boolean isInvulnerable() {
		return true;
	}

	public void inactiveTick() {
		if (!this.lockTick) {
			super.inactiveTick();
		}
	}

	public void setLockTick(boolean lock) {
		this.lockTick = lock;
	}

	public void die() {
		setLockTick(false);
		super.die();
	}

	public boolean isDeadNMS() {
		return this.dead;
	}

	public void killEntityNMS() {
		die();
	}

	public void setLocationNMS(double x, double y, double z) {
		super.setPosition(x, y, z);
	}

	public void setItemStackNMS(org.bukkit.inventory.ItemStack stack) {
		net.minecraft.server.v1_7_R4.ItemStack newItem = CraftItemStack.asNMSCopy(stack);
		if (newItem == null) {
			newItem = new net.minecraft.server.v1_7_R4.ItemStack(Blocks.BEDROCK);
		}
		if (newItem.tag == null) {
			newItem.tag = new NBTTagCompound();
		}
		NBTTagCompound display = newItem.tag.getCompound("display");
		if (!newItem.tag.hasKey("display")) {
			newItem.tag.set("display", display);
		}
		NBTTagList tagList = new NBTTagList();
		tagList.add(new NBTTagString(ChatColor.BLACK.toString() + Math.random()));

		display.set("Lore", tagList);
		newItem.count = 0;
		setItemStack(newItem);
	}

	public void setPassengerOfNMS(Entity entity) {
		try {
			AccessUtil.setAccessible(net.minecraft.server.v1_7_R4.Entity.class.getDeclaredField("g")).set(this, Double.valueOf(0.0D));
			AccessUtil.setAccessible(net.minecraft.server.v1_7_R4.Entity.class.getDeclaredField("h")).set(this, Double.valueOf(0.0D));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (this.vehicle != null) {
			this.vehicle.passenger = null;
		}
		this.vehicle = entity;
		entity.passenger = this;
	}
}