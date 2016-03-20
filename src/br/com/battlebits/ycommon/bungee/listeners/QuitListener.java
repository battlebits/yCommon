package br.com.battlebits.ycommon.bungee.listeners;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class QuitListener implements Listener {

	@EventHandler
	public void onQuit(PlayerDisconnectEvent event) {
		final ProxiedPlayer p = event.getPlayer();
		ProxyServer.getInstance().getScheduler().runAsync(BungeeMain.getPlugin(), new Runnable() {
			@Override
			public void run() {
				BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
				player.setLeaveData();
				BattlebitsAPI.getAccountCommon().saveBattlePlayer(player);
				player = null;
			}
		});
	}

}
