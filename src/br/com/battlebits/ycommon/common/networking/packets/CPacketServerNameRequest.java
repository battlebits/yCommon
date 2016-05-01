package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerNameRequest extends CommonPacket {

	private String serverListening;
	
	public CPacketServerNameRequest() {
		// TODO Auto-generated constructor stub
	}
	
	public CPacketServerNameRequest(String serverListening) {
		this.serverListening = serverListening;
	}
	
	public String getServerListening() {
		return serverListening;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.serverListening = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(serverListening);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleServerRequest(this);
	}

}
