package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.UUID;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketRemoveBlockedPlayer extends CommonPacket {

	private UUID player;
	private UUID unblocked;

	public CPacketRemoveBlockedPlayer() {
	}

	public CPacketRemoveBlockedPlayer(UUID player, UUID unblocked) {
		this.player = player;
		this.unblocked = unblocked;
	}

	public UUID getPlayer() {
		return player;
	}

	public UUID getUnblocked() {
		return unblocked;
	}

	@Override
	public void read(DataInputStream in) throws Exception {
		this.player = UUID.fromString(in.readUTF());
		this.unblocked = UUID.fromString(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(player.toString());
		out.writeUTF(unblocked.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleUnblockPlayer(this);
	}

}
