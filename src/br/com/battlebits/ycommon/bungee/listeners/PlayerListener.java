package br.com.battlebits.ycommon.bungee.listeners;

import java.util.Iterator;

import br.com.battlebits.ycommon.bungee.event.UpdateEvent;
import br.com.battlebits.ycommon.bungee.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerListener implements Listener {

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (BungeeCord.getInstance().getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}
	}

	@EventHandler(priority = (byte) -128)
	public void onConnect(ServerConnectedEvent event) {
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		player.connect(event.getServer().getInfo().getName());
	}
}
