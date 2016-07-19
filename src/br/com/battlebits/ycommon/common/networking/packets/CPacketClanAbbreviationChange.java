package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketClanAbbreviationChange extends CommonPacket {

	private String clanName;
	private String abbreviation;

	public CPacketClanAbbreviationChange() {
	}

	public CPacketClanAbbreviationChange(String clanName, String abbreviation) {
		this.clanName = clanName;
		this.abbreviation = abbreviation;
	}

	@Override
	public void read(DataInputStream in) throws IOException {
		this.clanName = in.readUTF();
		this.abbreviation = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(clanName);
		out.writeUTF(abbreviation);
	}

	public String getClanName() {
		return clanName;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handlerClanAbbreviationChange(this);
	}

}