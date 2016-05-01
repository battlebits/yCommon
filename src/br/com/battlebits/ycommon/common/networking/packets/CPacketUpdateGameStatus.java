package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketUpdateGameStatus extends CommonPacket {

	private UUID uuid;
	private String gameType;
	private String json;

	public CPacketUpdateGameStatus() {
	}

	public CPacketUpdateGameStatus(UUID uuid, String gameType, String json) {
		this.uuid = uuid;
		this.gameType = gameType;
		this.json = json;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getGameType() {
		return gameType;
	}

	public String getJson() {
		return json;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.uuid = UUID.fromString(in.readUTF());
		this.gameType = in.readUTF();
		this.json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(uuid.toString());
		out.writeUTF(gameType);
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleUpdateGameStatus(this);
	}

}
