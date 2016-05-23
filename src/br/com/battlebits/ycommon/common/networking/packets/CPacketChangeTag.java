package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.tag.Tag;

public class CPacketChangeTag extends CommonPacket {
	private UUID uniqueId;
	private Tag tag;

	public CPacketChangeTag() {
	}

	public CPacketChangeTag(UUID id, Tag tag) {
		this.uniqueId = id;
		this.tag = tag;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.uniqueId = UUID.fromString(in.readUTF());
		this.tag = Tag.valueOf(in.readUTF());
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(uniqueId.toString());
		out.writeUTF(tag.toString());
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleChangeTag(this);
	}

	public UUID getUniqueId() {
		return uniqueId;
	}

	public Tag getTag() {
		return tag;
	}

}
