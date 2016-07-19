package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketPlayerLeaveClan extends CommonPacket {
	private UUID uuid;

	public CPacketPlayerLeaveClan() {
	}

	public CPacketPlayerLeaveClan(UUID uuid) {
		this.uuid = uuid;
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.uuid = UUID.fromString(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(uuid.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handlerPlayerLeaveClan(this);
	}

}
