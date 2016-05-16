package br.com.battlebits.ycommon.bungee.listeners;

import br.com.battlebits.ycommon.bungee.managers.ServerManager;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoadBalancerListener implements Listener {

	private ServerManager manager;

	public LoadBalancerListener(ServerManager manager) {
		this.manager = manager;
	}

	@EventHandler
	public void onLogin(ServerConnectEvent event) {
		BattleServer lobby = manager.getLobbyBalancer().next();
		if (lobby != null) {
			event.setTarget(lobby.getServerInfo());
		}
	}
}
