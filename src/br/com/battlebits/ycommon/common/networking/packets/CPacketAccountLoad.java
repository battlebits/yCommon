package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketAccountLoad extends CommonPacket {

	private String json;
	public CPacketAccountLoad() {
	}

	public CPacketAccountLoad(BattlePlayer player) {
		json = BattlebitsAPI.getGson().toJson(player);
	}

	public BattlePlayer getBattlePlayer() {
		return BattlebitsAPI.getGson().fromJson(json, BattlePlayer.class);
	}
	
	public BukkitPlayer getBukkitPlayer() {
		return BattlebitsAPI.getGson().fromJson(json, BukkitPlayer.class);
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleAccountLoad(this);
	}

}
