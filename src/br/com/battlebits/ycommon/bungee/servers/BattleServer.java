package br.com.battlebits.ycommon.bungee.servers;

import br.com.battlebits.ycommon.bungee.loadbalancer.LoadBalancerObject;
import br.com.battlebits.ycommon.bungee.loadbalancer.NumberConnection;
import br.com.battlebits.ycommon.common.enums.ServerType;

public class BattleServer implements LoadBalancerObject, NumberConnection {

	private String serverId;
	
	private int onlinePlayers;
	private int maxPlayers;

	private boolean joinEnabled;


	public BattleServer(int onlinePlayers, int maxPlayers, boolean joinEnabled) {
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
	
	public void setJoinEnabled(boolean joinEnabled) {
		this.joinEnabled = joinEnabled;
	}

	public boolean isJoinEnabled() {
		return joinEnabled;
	}

	public ServerType getServerType() {
		return ServerType.getServerType(serverId);
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
