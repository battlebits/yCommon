package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
	public void read(DataInputStream in) throws IOException {
		this.player = UUID.fromString(in.readUTF());
		this.unblocked = UUID.fromString(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(player.toString());
		out.writeUTF(unblocked.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleUnblockPlayer(this);
	}

}
