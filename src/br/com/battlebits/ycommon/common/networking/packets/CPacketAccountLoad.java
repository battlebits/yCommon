package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketAccountLoad extends CommonPacket {

	private UUID uuid;
	private String json;

	public CPacketAccountLoad() {
	}

	public CPacketAccountLoad(UUID uuid, String json) {
		this.uuid = uuid;
		this.json = json;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getJson() {
		return json;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		uuid = UUID.fromString(in.readUTF());
		json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(uuid.toString());
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleAccountLoad(this);
	}

}
