package br.com.battlebits.ycommon.bukkit.api.inventory.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClickListener(InventoryClickEvent e) {
		if (e.getInventory() != null) {
			if (e.getInventory().getType() == InventoryType.CHEST) {
				if (e.getInventory().getHolder() != null) {
					if (e.getInventory().getHolder() instanceof MenuHolder) {
						if (e.getClickedInventory() == e.getInventory()) {
							if (e.getWhoClicked() instanceof Player) {
								if (e.getSlot() >= 0) {
									MenuHolder holder = (MenuHolder) e.getInventory().getHolder();
									MenuInventory menu = holder.getMenu();
									if (menu.hasItem(e.getSlot())) {
										Player p = (Player) e.getWhoClicked();
										MenuItem item = menu.getItem(e.getSlot());
										item.getHandler().onClick(p, e.getInventory(), ((e.getAction() == InventoryAction.PICKUP_HALF) ? ClickType.RIGHT : ClickType.LEFT), e.getCurrentItem(), e.getSlot());
										item = null;
										p = null;
									}
									menu = null;
									holder = null;
								}
							}
						}
						e.setCancelled(true);
					}
				}
			}
		}
	}

}
