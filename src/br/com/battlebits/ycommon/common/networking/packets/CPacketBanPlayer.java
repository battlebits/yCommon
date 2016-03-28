package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.banmanager.constructors.Ban;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketBanPlayer extends CommonPacket {

	private Ban ban;
	
	public CPacketBanPlayer() {
	}
	
	public CPacketBanPlayer(Ban ban) {
		this.ban = ban;
	}
	
	public Ban getBan() {
		return ban;
	}
	
	@Override
	public void read(DataInputStream in) throws Exception {
		this.ban = BattlebitsAPI.getGson().fromJson(in.readUTF(), Ban.class);
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(BattlebitsAPI.getGson().toJson(ban));
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleBanPlayer(this);
	}

}
