package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketAddBlockedPlayer extends CommonPacket {

	private UUID playerUUID;
	private Blocked blocked;

	public CPacketAddBlockedPlayer() {
	}

	public CPacketAddBlockedPlayer(UUID player, Blocked blocked) {
		this.playerUUID = player;
		this.blocked = blocked;
	}

	public UUID getPlayerUUID() {
		return playerUUID;
	}

	public Blocked getBlocked() {
		return blocked;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.playerUUID = UUID.fromString(in.readUTF());
		this.blocked = BattlebitsAPI.getGson().fromJson(in.readUTF(), Blocked.class);
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(playerUUID.toString());
		out.writeUTF(BattlebitsAPI.getGson().toJson(blocked));
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleBlockPlayer(this);
	}

}
