package br.com.battlebits.ycommon.bukkit.accounts;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class AccountListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsync(AsyncPlayerPreLoginEvent event) throws Exception {
		BukkitMain.getPlugin().getAccountManager().loadPlayer(event.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onRemoveAccount(AsyncPlayerPreLoginEvent event) {
		if (event.getLoginResult() != Result.ALLOWED)
			BattlebitsAPI.getAccountCommon().unloadBattlePlayer(event.getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onLeave(PlayerQuitEvent event) {
		BattlebitsAPI.getAccountCommon().unloadBattlePlayer(event.getPlayer().getUniqueId());
	}
}
