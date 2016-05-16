package br.com.battlebits.ycommon.bungee.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.loadbalancer.BaseBalancer;
import br.com.battlebits.ycommon.bungee.loadbalancer.LeastConnection;
import br.com.battlebits.ycommon.bungee.networking.BungeeClient;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.connection.backend.MySQLBackend;

public class ServerManager {

	private Map<String, String> battlebitsServers;

	private Map<String, BattleServer> activeServers;

	private BaseBalancer<BattleServer> fullIronBalancer;
	
	private BaseBalancer<BattleServer> peladoBalancer;
	
	private BaseBalancer<BattleServer> hgBalancer;
	
	public ServerManager(BungeeMain main) {
		fullIronBalancer = new LeastConnection<>();
		peladoBalancer = new LeastConnection<>();
		hgBalancer = new LeastConnection<>();
		battlebitsServers = new HashMap<>();
		activeServers = new HashMap<>();
		loadServers(main.getConnection());
	}

	private void loadServers(MySQLBackend backend) {
		try {
			BattlebitsAPI.debug("SERVERS > LOADING");
			PreparedStatement stmt = backend.getConnection().prepareStatement("SELECT * FROM `servers`;");
			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				try {
					battlebitsServers.put(result.getString("serverAddress"), result.getString("serverId"));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("TRANSLATIONS > CLOSE");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getServerId(String serverAddress) {
		return battlebitsServers.containsKey(serverAddress) ? battlebitsServers.get(serverAddress) : serverAddress;
	}

	public void addActiveServer(String str, BungeeClient client, int maxPlayers) {
		BungeeMain.getPlugin().addBungee(client.getServerIp(), str.split(":")[0], Integer.valueOf(str.split(":")[1]));
		updateActiveServer(client.getServerIp(), client, 0, maxPlayers);
	}

	public void updateActiveServer(String str, BungeeClient client, int onlinePlayers, int maxPlayers) {
		BattleServer server = null;
		if(str.endsWith("battle-hg.com")) {
			server = new HungerGamesServer(client, BungeeMain.getPlugin().getProxy().getServerInfo(str), onlinePlayers, true);
		} else {
			server = new BattleServer(client, BungeeMain.getPlugin().getProxy().getServerInfo(str), onlinePlayers, maxPlayers, true);
		}
		updateActiveServer(str, server);
		activeServers.put(str, server);
	}

	public BattleServer getServer(String str) {
		return activeServers.get(str);
	}

	public void removeActiveServer(String str) {
		activeServers.remove(str);
	}
	
	
	
	public void updateActiveServer(String serverId, BattleServer server) {
		if(serverId.endsWith("fulliron.battlecraft.com.br")) {
			fullIronBalancer.add(serverId, server);
		} else if(serverId.endsWith("simulator.battlecraft.com.br")) {
			peladoBalancer.add(serverId, server);
		} else if(serverId.endsWith("battle-hg.com")) {
			hgBalancer.add(serverId, server);
		}
	}

}
