package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

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
	public void read(DataInputStream in) throws Exception {
		this.serverName = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(serverName);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleServerLoad(this);
	}

}
