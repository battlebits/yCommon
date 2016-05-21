package br.com.battlebits.ycommon.bungee.listeners;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.managers.ServerManager;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.md_5.bungee.api.Callback;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Players;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ProxyPingEvent;
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
		ProxiedPlayer player = event.getPlayer();
		if (player.getPendingConnection() == null)
			return;
		if (player.getServer() != null)
			return;
		String serverIp = getServerIp(event.getPlayer().getPendingConnection());
		BattleServer server = manager.getServer(serverIp);
		if (server != null) {
			event.setTarget(server.getServerInfo());
			return;
		}

		ServerType type = ServerType.getServerType(serverIp);
		ServerInfo toConnect = null;
		switch (type) {
		case PVP_FULLIRON: {
			server = manager.getFullIronBalancer().next();
			if (server != null)
				toConnect = server.getServerInfo();
		}
		case PVP_SIMULATOR: {
			server = manager.getPeladoBalancer().next();
			if (server != null)
				toConnect = server.getServerInfo();
		}
		case HUNGERGAMES: {
			server = manager.getHgBalancer().next();
			if (server != null)
				toConnect = server.getServerInfo();
		}
		default:
			break;
		}
		if (toConnect != null) {
			event.setTarget(toConnect);
		} else {
			BattleServer lobby = manager.getLobbyBalancer().next();
			if (lobby != null && lobby.getServerInfo() != null) {
				event.setTarget(lobby.getServerInfo());
			} else {
				player.disconnect(TextComponent.fromLegacyText(Translate.getTranslation(BattlebitsAPI.getAccountCommon().getBattlePlayer(player.getUniqueId()).getLanguage(), "server-not-available")));
			}
		}
	}

	@EventHandler(priority = -128)
	public void onProxyPing(ProxyPingEvent event) {
		String serverIp = getServerIp(event.getConnection());
		BattleServer server = manager.getServer(serverIp);
		ServerPing ping = event.getResponse();
		ping.getVersion().setName("Join with 1.7 or 1.8");
		if (server != null) {
			event.registerIntent(BungeeMain.getPlugin());
			server.getServerInfo().ping(new Callback<ServerPing>() {
				@Override
				public void done(ServerPing realPing, Throwable throwable) {
					if (throwable != null) {
						ping.getPlayers().setMax(realPing.getPlayers().getMax());
						ping.getPlayers().setOnline(realPing.getPlayers().getOnline());
						ping.setDescription(realPing.getDescription());
					} else {
						ping.setPlayers(new Players(-1, -1, null));
						ping.setVersion(new Protocol("Server not found", 0));
						ping.setDescription(ChatColor.RED + "Error :(");
					}
					event.completeIntent(BungeeMain.getPlugin());
				}
			});
		}
	}

	private String getServerIp(PendingConnection con) {
		if (con == null)
			return "";
		if (con.getVirtualHost() == null)
			return "";
		String s = con.getVirtualHost().getHostName().toLowerCase();
		if (s.isEmpty())
			return "";
		for (int i = 0; i < 10; i++) {
			s = s.replace("proxy" + i + ".", "");
		}
		for (String str : new String[] { "br.", "us.", "eu." }) {
			s = s.replace(str, "");
		}
		return s;
	}
}
