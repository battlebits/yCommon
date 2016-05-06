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
	private Inventory inv;
	private boolean onePerPlayer;

	public MenuInventory(String title, int rows) {
		this(title, rows, false);
	}

	public MenuInventory(String title, int rows, boolean onePerPlayer) {
		this.slotItem = new HashMap<>();
		this.rows = rows;
		this.title = title;
		this.onePerPlayer = onePerPlayer;
		if (!onePerPlayer) {
			this.inv = Bukkit.createInventory(new MenuHolder(this), rows * 9, "");
		}
	}

	public void addItem(MenuItem item) {
		setItem(item, firstEmpty());
	}

	public void addItem(ItemStack item) {
		setItem(item, firstEmpty());
	}

	public void setItem(ItemStack item, int slot) {
		setItem(new MenuItem(item), slot);
	}

	public void setItem(MenuItem item, int slot) {
		this.slotItem.put(slot, item);
		if (!onePerPlayer) {
			inv.setItem(slot, item.getStack());
		}
	}

	public int firstEmpty() {
		if (!onePerPlayer) {
			return inv.firstEmpty();
		} else {
			for (int i = 0; i < rows * 9; i++) {
				if (!slotItem.containsKey(i)) {
					return i;
				}
			}
			return -1;
		}
	}

	public boolean hasItem(int slot) {
		return this.slotItem.containsKey(slot);
	}

	public MenuItem getItem(int slot) {
		return this.slotItem.get(slot);
	}

	public void open(Player p) {
		if (!onePerPlayer) {
			p.openInventory(inv);
		} else {
			if (p.getOpenInventory() == null || p.getOpenInventory().getTopInventory().getType() != InventoryType.CHEST
					|| p.getOpenInventory().getTopInventory().getSize() != rows * 9 || p.getOpenInventory().getTopInventory().getHolder() == null
					|| !(p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder)
					|| !(((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).isOnePerPlayer())) {
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
				p.updateInventory();
			}
			((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).setMenu(this);
		}
		updateTitle(p);
		// Garbage Colector
		p = null;
	}

	public void updateTitle(Player p) {
		EntityPlayer ep = ((CraftPlayer) p).getHandle();
		PacketPlayOutOpenWindow openWindow = new PacketPlayOutOpenWindow(ep.activeContainer.windowId, 0, this.title, rows * 9, false);
		ep.playerConnection.sendPacket(openWindow);
		ep.updateInventory(ep.activeContainer);
		// Garbage Colector
		int i = 0;
		for (ItemStack item : p.getInventory().getContents()) {
			p.getInventory().setItem(i, item);
			i += 1;
		}
		p.updateInventory();
		openWindow = null;
		ep = null;
	}

	public void createAndOpenInventory(Player p) {
		// Create a New Inventory
		Inventory playerInventory = Bukkit.createInventory(new MenuHolder(this), rows * 9, "");
		for (Entry<Integer, MenuItem> entry : slotItem.entrySet()) {
			playerInventory.setItem(entry.getKey(), entry.getValue().getStack());
		}
		p.openInventory(playerInventory);
		// Garbage Colector
		p = null;
	}

	public void close(Player p) {
		if (onePerPlayer) {
			destroy(p);
			p = null;
		}
	}

	public void destroy(Player p) {
		if (p.getOpenInventory().getTopInventory().getHolder() != null && p.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
			((MenuHolder) p.getOpenInventory().getTopInventory().getHolder()).destroy();
		}
	}

	// public void destroy() {
	// for (MenuItem item : slotItem.values()) {
	// item.destroy();
	// }
	// this.slotItem.clear();
	// this.slotItem = null;
	// }

	public boolean isOnePerPlayer() {
		return onePerPlayer;
	}

	public Inventory getInventory() {
		return inv;
	}

}
