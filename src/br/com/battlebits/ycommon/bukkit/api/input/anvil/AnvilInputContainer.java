package br.com.battlebits.ycommon.bukkit.api.input.anvil;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.ContainerAnvil;
import net.minecraft.server.v1_7_R4.EntityHuman;

class AnvilInputContainer extends ContainerAnvil {

	public AnvilInputContainer(Player p) {
		super(((CraftPlayer) p).getHandle().inventory, ((CraftPlayer) p).getHandle().world, p.getLocation().getBlockX(), p.getLocation().getBlockY(),
				p.getLocation().getBlockZ(), ((CraftPlayer) p).getHandle());
	}

	@Override
	public boolean a(EntityHuman entityhuman) {
		return true;
	}

}