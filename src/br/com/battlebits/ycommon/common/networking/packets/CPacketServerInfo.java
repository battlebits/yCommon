package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer.HungerGamesState;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerInfo extends CommonPacket {

	private int onlinePlayers;
	private int maxPlayers;

	private boolean canJoin;

	private int tempo;

	private HungerGamesState hungerGamesStatus;

	public CPacketServerInfo() {
	}

	public CPacketServerInfo(int onlinePlayers, int maxPlayers, boolean canJoin) {
		this(onlinePlayers, maxPlayers, canJoin, -1, HungerGamesState.NONE);
	}

	public CPacketServerInfo(int onlinePlayers, int maxPlayers, boolean canJoin, int tempo, HungerGamesState hungerGamesStatus) {
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
		this.canJoin = canJoin;
		this.tempo = tempo;
		this.hungerGamesStatus = hungerGamesStatus;
	}

	public HungerGamesState getHungerGamesStatus() {
		return hungerGamesStatus;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getTempo() {
		return tempo;
	}

	public boolean canJoin() {
		return canJoin;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.onlinePlayers = in.readInt();
		this.maxPlayers = in.readInt();
		this.canJoin = in.readBoolean();
		this.tempo = in.readInt();
		this.hungerGamesStatus = HungerGamesState.valueOf(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeInt(onlinePlayers);
		out.writeInt(maxPlayers);
		out.writeBoolean(canJoin);
		out.writeInt(tempo);
		out.writeUTF(hungerGamesStatus.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleServerInfo(this);
	}

}
