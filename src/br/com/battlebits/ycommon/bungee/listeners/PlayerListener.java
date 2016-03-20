package br.com.battlebits.ycommon.bungee.listeners;

import java.util.Iterator;

import org.bukkit.Bukkit;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent;
import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		if (players.hasNext()) {
			BattlePlayer player = players.next();
			if (Bukkit.getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
				}
			}
		}
	}
}
