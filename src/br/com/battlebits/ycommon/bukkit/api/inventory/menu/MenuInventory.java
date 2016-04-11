package br.com.battlebits.ycommon.bukkit.api.inventory.menu;

import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PacketPlayOutOpenWindow;

public class MenuInventory {

	private HashMap<Integer, MenuItem> slotItem;
	private int rows;
	private String title;
	private boolean isTranslateId;

	public MenuInventory(String title, int rows) {
		this(title, rows, false);
	}

	public MenuInventory(String title, int rows, boolean isTranslated) {
		this.slotItem = new HashMap<>();
		this.rows = rows;
		this.title = title;
		this.isTranslateId = isTranslated;
	}

	public void addItem(MenuItem item) {
		setItem(item, firstEmpty());
	}

	public void setItem(MenuItem item, int slot) {
		this.slotItem.put(slot, item);
	}

	public void addItem(ItemStack item) {
		setItem(item, firstEmpty());
	}

	public void setItem(ItemStack item, int slot) {
		MenuItem mItem = new MenuItem(item);
		this.slotItem.put(slot, mItem);
	}

	public int firstEmpty() {
		for (int i = 0; i < rows * 9; i++) {
			if (!slotItem.containsKey(i)) {
				return i;
			}
		}
		return -1;
	}

	public boolean hasItem(int slot) {
		return this.slotItem.containsKey(slot);
	}

	public MenuItem getItem(int slot) {
		return this.slotItem.get(slot);
	}

	public void open(Player p) {
		if (p.getOpenInventory() == null || p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST || p.getOpenInventory().getTopInventory().getSize() < rows * 9 || p.getOpenInventory().getTopInventory().getHolder() == null || !(p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder)) {
			createAndOpenInventory(p);
		} else {
			// Update the current inventory of player
			for (int i = 0; i < rows * 9; i++) {
				if (slotItem.containsKey(i)) {
					p.getOpenInventory().getTopInventory().setItem(i, slotItem.get(i).getStack());
				} else {
					p.getOpenInventory().getTopInventory().setItem(i, null);
				}
			}
			updateTitle(p);
		}
		((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).setMenu(this);
		// Garbage Colector
		p = null;
	}

	public void updateTitle(Player p) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, (isTranslateId ? "translateId:" : "") + title, rows * 9, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);
		// Garbage Colector
		openWindow = null;
		ep = null;
	}

	public void createAndOpenInventory(Player p) {
		// Create a New Inventory
		Inventory playerInventory = Bukkit.createInventory(new MenuHolder(this), rows * 9, (isTranslateId ? "translateId:" : "") + this.title);
		for (Entry<Integer, MenuItem> entry : slotItem.entrySet()) {
			playerInventory.setItem(entry.getKey(), entry.getValue().getStack());
		}
		p.openInventory(playerInventory);
		// Garbage Colector
		p = null;
	}

	public void close(Player p) {
		destroy(p);
		p = null;
	}

	public void destroy(Player p) {
		if (p.getOpenInventory().getTopInventory().getHolder() != null && p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
			((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).destroy();
		}
	}

	public void destroy() {
		for (MenuItem item : slotItem.values()) {
			item.destroy();
		}
		this.slotItem.clear();
		this.slotItem = null;
	}

}
