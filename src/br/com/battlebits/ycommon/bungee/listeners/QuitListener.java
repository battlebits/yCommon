package br.com.battlebits.ycommon.bungee.listeners;

import java.sql.SQLException;

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
				String json = BungeeMain.getGson().toJson(player);
				try {
					BungeeMain.getPlugin().getConnection().update("INSERT INTO `account`(`uuid`, `json`) VALUES ('" + p.getUniqueId().toString().replace("-", "") + "','" + json + "') ON DUPLICATE KEY UPDATE `json` ='" + json + "';");
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					e.printStackTrace();
				}
				BattlebitsAPI.getAccountCommon().unloadBattlePlayer(p.getUniqueId());
				json = null;
				player = null;
			}
		});
	}

}
