package br.com.battlebits.ycommon.bungee.servers;

import br.com.battlebits.ycommon.bungee.loadbalancer.LoadBalancerObject;
import br.com.battlebits.ycommon.bungee.loadbalancer.NumberConnection;
import br.com.battlebits.ycommon.bungee.networking.BungeeClient;
import br.com.battlebits.ycommon.common.enums.ServerType;
import net.md_5.bungee.api.config.ServerInfo;

public class BattleServer implements LoadBalancerObject, NumberConnection {

	private int onlinePlayers;
	private int maxPlayers;

	private boolean joinEnabled;

	private BungeeClient serverClient;

	private ServerInfo serverInfo;

	public BattleServer(BungeeClient client, ServerInfo info, int onlinePlayers, int maxPlayers, boolean joinEnabled) {
		this.serverClient = client;
		this.serverInfo = info;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.joinEnabled = joinEnabled;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public boolean isFull() {
		return onlinePlayers >= maxPlayers;
	}

	public boolean isJoinEnabled() {
		return joinEnabled;
	}

	public BungeeClient getServerClient() {
		return serverClient;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	
	public ServerType getServerType() {
		return ServerType.getServerType(serverClient.getServerIp());
	}

	@Override
	public boolean canBeSelected() {
		return !isFull() && isJoinEnabled();
	}

	@Override
	public int getActualNumber() {
		return getOnlinePlayers();
	}

	@Override
	public int getMaxNumber() {
		return getMaxPlayers();
	}
}
