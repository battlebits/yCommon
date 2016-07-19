package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketClanLoad extends CommonPacket {

	private String json;

	public CPacketClanLoad() {
	}

	public CPacketClanLoad(Clan clan) {
		json = BattlebitsAPI.getGson().toJson(clan);
	}

	public Clan getClan() {
		return BattlebitsAPI.getGson().fromJson(json, Clan.class);
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleClanLoad(this);
	}

}
