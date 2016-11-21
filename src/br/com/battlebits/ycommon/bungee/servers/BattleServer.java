package br.com.battlebits.ycommon.bungee.servers;

import br.com.battlebits.ycommon.bungee.BungeeMain;
import br.com.battlebits.ycommon.bungee.loadbalancer.elements.LoadBalancerObject;
import br.com.battlebits.ycommon.bungee.loadbalancer.elements.NumberConnection;
import br.com.battlebits.ycommon.common.enums.ServerType;
import net.md_5.bungee.api.config.ServerInfo;

public class BattleServer implements LoadBalancerObject, NumberConnection {

	private String serverId;

	private int onlinePlayers;
	private int maxPlayers;

	private boolean joinEnabled;

	public BattleServer(String serverId, int onlinePlayers, int maxPlayers, boolean joinEnabled) {
		this.serverId = serverId;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.joinEnabled = joinEnabled;
	}

	public void setOnlinePlayers(int onlinePlayers) {
		this.onlinePlayers = onlinePlayers;
	}
	
	public String getServerId() {
		return serverId;
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

	public void setJoinEnabled(boolean joinEnabled) {
		this.joinEnabled = joinEnabled;
	}

	public boolean isJoinEnabled() {
		return joinEnabled;
	}

	public ServerInfo getServerInfo() {
		return BungeeMain.getPlugin().getProxy().getServerInfo(serverId);
	}

	public ServerType getServerType() {
		return ServerType.getServerType(serverId);
	}

	@Override
	public boolean canBeSelected() {
		return isJoinEnabled();
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
