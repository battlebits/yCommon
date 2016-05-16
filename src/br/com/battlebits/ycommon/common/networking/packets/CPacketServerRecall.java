package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerRecall extends CommonPacket {

	private int onlinePlayers;
	private int maxPlayers;

	private String serverAddress;

	public CPacketServerRecall() {

	}

	public CPacketServerRecall(String serverListening, int onlinePlayers, int maxPlayers) {
		this.serverAddress = serverListening;
		this.onlinePlayers = onlinePlayers;
		this.maxPlayers = maxPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public int getOnlinePlayers() {
		return onlinePlayers;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.serverAddress = in.readUTF();
		this.onlinePlayers = in.readInt();
		this.maxPlayers = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(serverAddress);
		out.writeInt(onlinePlayers);
		out.writeInt(maxPlayers);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleServerRecall(this);
	}

}
