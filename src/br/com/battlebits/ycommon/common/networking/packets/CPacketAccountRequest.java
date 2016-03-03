package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketAccountRequest extends CommonPacket {
	private UUID uuid;

	public CPacketAccountRequest() {
	}

	public CPacketAccountRequest(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.uuid = UUID.fromString(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(uuid.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleAccountRequest(this);
	}

}
