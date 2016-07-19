package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketPlayerJoinClan extends CommonPacket {

	private UUID uuid;
	private String clanName;

	public CPacketPlayerJoinClan() {
	}

	public CPacketPlayerJoinClan(UUID uuid, String clanName) {
		this.uuid = uuid;
		this.clanName = clanName;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.uuid = UUID.fromString(in.readUTF());
		this.clanName = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(uuid.toString());
		out.writeUTF(clanName);
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getClanName() {
		return clanName;
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handlerPlayerJoinClan(this);
	}

}
