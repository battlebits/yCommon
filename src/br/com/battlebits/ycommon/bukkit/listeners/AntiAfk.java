package br.com.battlebits.ycommon.bukkit.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;

public class AntiAfk implements Listener {
	public Map<UUID, Location> locations = new HashMap<>();
	private static Map<UUID, Integer> afkMap = new HashMap<>();

	public void addTime(Player p) {
		int time = 0;
		if (afkMap.containsKey(p.getUniqueId()))
			time = afkMap.get(p.getUniqueId());
		afkMap.put(p.getUniqueId(), ++time);
	}

	public static int getTime(Player p) {
		return afkMap.containsKey(p.getUniqueId()) ? afkMap.get(p.getUniqueId()) : 0;
	}

	public void resetTimer(Player p) {
		afkMap.remove(p.getUniqueId());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitListener(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		for (Player p : Bukkit.getOnlinePlayers()) {
			Location loc = locations.get(p.getUniqueId());
			if (loc == null) {
				locations.put(p.getUniqueId(), p.getLocation().clone());
				continue;
			}
			Location l = p.getLocation();
			if (loc.getX() == l.getX() && loc.getY() == l.getY() && loc.getZ() == l.getZ()) {
				addTime(p);
			} else {
				resetTimer(p);
			}
			BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			if (!player.hasGroupPermission(Group.LIGHT)) {
				if (getTime(p) == 240) {
					p.sendMessage("§%antiafk-timer%§");
					break;
				}
				if (getTime(p) >= 300) {
					p.kickPlayer(Translate.getTranslation(player.getLanguage(), "antiafk-kick"));
					break;
				}
			}
			locations.put(p.getUniqueId(), p.getLocation().clone());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerQuitEvent e) {
		locations.remove(e.getPlayer().getUniqueId());
	}

}
