package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerStart extends CommonPacket {

	private String serverAddress;

	private int maxPlayers;

	public CPacketServerStart() {
		// TODO Auto-generated constructor stub
	}

	public CPacketServerStart(String serverListening, int maxPlayers) {
		this.serverAddress = serverListening;
		this.maxPlayers = maxPlayers;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.serverAddress = in.readUTF();
		this.maxPlayers = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(serverAddress);
		out.writeInt(maxPlayers);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleServerStart(this);
	}

}
