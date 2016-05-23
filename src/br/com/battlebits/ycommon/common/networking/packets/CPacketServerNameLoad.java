package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerNameLoad extends CommonPacket {

	private String serverName;

	public CPacketServerNameLoad() {
	}

	public CPacketServerNameLoad(String serverName) {
		this.serverName = serverName;
	}

	public String getServerHostName() {
		return serverName;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.serverName = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(serverName);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleServerLoad(this);
	}

}
