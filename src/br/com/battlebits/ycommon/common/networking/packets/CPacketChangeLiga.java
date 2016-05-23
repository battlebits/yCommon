package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketChangeLiga extends CommonPacket {

	private UUID uuid;
	private Liga liga;

	public CPacketChangeLiga() {
	}

	public CPacketChangeLiga(UUID uuid, Liga liga) {
		this.uuid = uuid;
		this.liga = liga;
	}

	public UUID getUuid() {
		return uuid;
	}

	public Liga getLiga() {
		return liga;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.uuid = UUID.fromString(in.readUTF());
		this.liga = Liga.valueOf(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(uuid.toString());
		out.writeUTF(liga.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleChangeLiga(this);
	}

}
