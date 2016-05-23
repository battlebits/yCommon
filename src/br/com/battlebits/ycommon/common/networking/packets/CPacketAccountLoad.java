package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
	public void read(DataInputStream in) throws IOException {
		json = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(json);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleAccountLoad(this);
	}

}
