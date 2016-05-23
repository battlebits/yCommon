package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
	public void read(DataInputStream in) throws IOException {
		this.serverAddress = in.readUTF();
		this.maxPlayers = in.readInt();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(serverAddress);
		out.writeInt(maxPlayers);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleServerStart(this);
	}

}
