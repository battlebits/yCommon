package br.com.battlebits.ycommon.bukkit.api.input.anvil;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

public class AnvilInputGui {

	private UUID playerUUID;
	private Inventory anvilInventory;
	private AnvilInputHandler inputHandler;
	private ItemStack searchItem;
	private boolean wasHandled;

	public AnvilInputGui(Player p, ItemStack item, AnvilInputHandler handler) {
		this.inputHandler = handler;
		this.playerUUID = p.getUniqueId();
		this.searchItem = item;
		this.wasHandled = false;
		open();
	}

	public void open() {
		EntityPlayer entityPlayer = ((CraftPlayer) getPlayer()).getHandle();
		AnvilInputContainer container = new AnvilInputContainer(getPlayer());
		anvilInventory = container.getBukkitView().getTopInventory();
		anvilInventory.setItem(0, searchItem);
		int c = entityPlayer.nextContainerCounter();
		entityPlayer.playerConnection.sendPacket(new PacketPlayOutOpenWindow(c, 8, "Repairing", 9, true));
		entityPlayer.activeContainer = container;
		entityPlayer.activeContainer.windowId = c;
		entityPlayer.activeContainer.addSlotListener(entityPlayer);
	}

	public void destroyThisInventory() {
		playerUUID = null;
		anvilInventory = null;
		inputHandler = null;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Player getPlayer() {
		return Bukkit.getPlayer(playerUUID);
	}

	public Inventory getAnvilInventory() {
		return anvilInventory;
	}

	public AnvilInputHandler getInputHandler() {
		return inputHandler;
	}

	public void setHandled(boolean b) {
		this.wasHandled = b;
	}

	public boolean wasHandled() {
		return wasHandled;
	}
}