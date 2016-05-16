package br.com.battlebits.ycommon.bungee.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.loadbalancer.BaseBalancer;
import br.com.battlebits.ycommon.bungee.loadbalancer.LeastConnection;
import br.com.battlebits.ycommon.bungee.loadbalancer.MostConnection;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class ServerManager {

	private Map<String, String> battlebitsServers;
	private Map<String, BattleServer> activeServers;

	private BaseBalancer<BattleServer> lobbyBalancer;
	private BaseBalancer<BattleServer> fullIronBalancer;
	private BaseBalancer<BattleServer> peladoBalancer;
	private BaseBalancer<BattleServer> hgBalancer;

	private BungeeMain main;

	public ServerManager(BungeeMain main) {
		this.main = main;
		lobbyBalancer = new LeastConnection<>();
		fullIronBalancer = new LeastConnection<>();
		peladoBalancer = new LeastConnection<>();
		hgBalancer = new MostConnection<>();
		battlebitsServers = new HashMap<>();
		activeServers = new HashMap<>();
		loadServers();
	}

	public void loadServers() {
		battlebitsServers.clear();
		try {
			BattlebitsAPI.debug("SERVERS > LOADING");
			PreparedStatement stmt = main.getConnection().getConnection().prepareStatement("SELECT * FROM `servers`;");
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

	public void addActiveServer(String serverAddress, String serverIp, int maxPlayers) {
		BungeeMain.getPlugin().addBungee(serverIp, serverAddress.split(":")[0], Integer.valueOf(serverAddress.split(":")[1]));
		updateActiveServer(serverIp, 0, maxPlayers, true);
	}

	public void updateActiveServer(String serverId, int onlinePlayers, int maxPlayers, boolean canJoin) {
		BattleServer server = activeServers.get(serverId);
		if (server == null) {
			if (serverId.endsWith("battle-hg.com")) {
				server = new HungerGamesServer(onlinePlayers, true);
			} else {
				server = new BattleServer(onlinePlayers, maxPlayers, true);
			}
			activeServers.put(serverId, server);
		}
		server.setOnlinePlayers(onlinePlayers);
		server.setJoinEnabled(canJoin);
	}

	public BattleServer getServer(String str) {
		return activeServers.get(str);
	}

	public void removeActiveServer(String str) {
		activeServers.remove(str);
	}

	public void updateBalancers(String serverId, BattleServer server) {
		if (serverId.endsWith("lobby.battlebits.com.br")) {
			lobbyBalancer.add(serverId, server);
		} else if (serverId.endsWith("fulliron.battlecraft.com.br")) {
			fullIronBalancer.add(serverId, server);
		} else if (serverId.endsWith("simulator.battlecraft.com.br")) {
			peladoBalancer.add(serverId, server);
		} else if (serverId.endsWith("battle-hg.com")) {
			hgBalancer.add(serverId, server);
		}
	}

}
