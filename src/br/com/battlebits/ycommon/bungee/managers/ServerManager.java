package br.com.battlebits.ycommon.bungee.managers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.loadbalancer.BaseBalancer;
import br.com.battlebits.ycommon.bungee.loadbalancer.types.LeastConnection;
import br.com.battlebits.ycommon.bungee.loadbalancer.types.MostConnection;
import br.com.battlebits.ycommon.bungee.servers.BattleServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer.HungerGamesState;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerManager {

	private Map<String, String> battlebitsServers;
	private Map<String, BattleServer> activeServers;

	private BaseBalancer<BattleServer> lobbyBalancer;
	private BaseBalancer<BattleServer> fullIronBalancer;
	private BaseBalancer<BattleServer> peladoBalancer;
	private BaseBalancer<HungerGamesServer> hgBalancer;

	private BungeeMain main;

	public ServerManager(BungeeMain main) {
		this.main = main;
		lobbyBalancer = new LeastConnection<>();
		fullIronBalancer = new MostConnection<>();
		peladoBalancer = new MostConnection<>();
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
					battlebitsServers.put(result.getString("serverAddress").toLowerCase(), result.getString("serverId").toLowerCase());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.close();
			stmt.close();
			result = null;
			stmt = null;
			BattlebitsAPI.debug("SERVERS > CLOSE");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getServerId(String serverAddress) {
		return battlebitsServers.containsKey(serverAddress.toLowerCase()) ? battlebitsServers.get(serverAddress.toLowerCase()) : serverAddress.toLowerCase();
	}

	public void addActiveServer(String serverAddress, String serverIp, int maxPlayers) {
		BungeeMain.getPlugin().addBungee(serverIp, serverAddress.split(":")[0], Integer.valueOf(serverAddress.split(":")[1]));
		updateActiveServer(serverIp, 0, maxPlayers, true);
	}

	public void sendAddToLobbies(String serverIp) {
		for (BattleServer server : lobbyBalancer.getList()) {
			sendDataToServer(server.getServerInfo(), "AddServer", serverIp.toLowerCase());
		}
	}

	public void sendRemoveToLobbies(String serverIp) {
		for (BattleServer server : lobbyBalancer.getList()) {
			sendDataToServer(server.getServerInfo(), "RemoveServer", serverIp.toLowerCase());
		}
	}

	public void sendDataToServer(ServerInfo info, String... data) {
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		for (String str : data) {
			out.writeUTF(str);
		}
		info.sendData("BungeeCord", out.toByteArray());
	}

	public void updateActiveServer(String serverId, int onlinePlayers, int maxPlayers, boolean canJoin) {
		updateActiveServer(serverId, onlinePlayers, maxPlayers, canJoin, 0, null);
	}

	public void updateActiveServer(String serverId, int onlinePlayers, int maxPlayers, boolean canJoin, int tempo, HungerGamesState state) {
		serverId = serverId.toLowerCase();
		BattleServer server = activeServers.get(serverId);
		if (server == null) {
			if (serverId.endsWith("battle-hg.com")) {
				server = new HungerGamesServer(serverId, onlinePlayers, true);
			} else {
				server = new BattleServer(serverId, onlinePlayers, maxPlayers, true);
			}
			activeServers.put(serverId, server);
		}
		server.setOnlinePlayers(onlinePlayers);
		server.setJoinEnabled(canJoin);
		if (state != null && server instanceof HungerGamesServer) {
			((HungerGamesServer) server).setState(state);
			((HungerGamesServer) server).setTempo(tempo);
		}
		addToBalancers(serverId, server);
	}

	public BattleServer getServer(String str) {
		return activeServers.get(str.toLowerCase());
	}

	public void removeActiveServer(String str) {
		removeFromBalancers(str);
		activeServers.remove(str.toLowerCase());
	}

	public void addToBalancers(String serverId, BattleServer server) {
		serverId = serverId.toLowerCase();
		if (serverId.endsWith("lobby.battlebits.com.br")) {
			lobbyBalancer.add(serverId, server);
		} else if (serverId.endsWith("fulliron.pvp.battlebits.com.br")) {
			fullIronBalancer.add(serverId, server);
		} else if (serverId.endsWith("simulator.pvp.battlebits.com.br")) {
			peladoBalancer.add(serverId, server);
		} else if (serverId.endsWith("battle-hg.com")) {
			hgBalancer.add(serverId, (HungerGamesServer) server);
		}
	}

	public void removeFromBalancers(String serverId) {
		serverId = serverId.toLowerCase();
		if (serverId.endsWith("lobby.battlebits.com.br")) {
			lobbyBalancer.remove(serverId);
		} else if (serverId.endsWith("fulliron.battlecraft.com.br")) {
			fullIronBalancer.remove(serverId);
		} else if (serverId.endsWith("simulator.battlecraft.com.br")) {
			peladoBalancer.remove(serverId);
		} else if (serverId.endsWith("battle-hg.com")) {
			hgBalancer.remove(serverId);
		}
	}

	public BaseBalancer<BattleServer> getFullIronBalancer() {
		return fullIronBalancer;
	}

	public BaseBalancer<HungerGamesServer> getHgBalancer() {
		return hgBalancer;
	}

	public BaseBalancer<BattleServer> getLobbyBalancer() {
		return lobbyBalancer;
	}

	public BaseBalancer<BattleServer> getPeladoBalancer() {
		return peladoBalancer;
	}

}
