package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketCreateParty extends CommonPacket {

	@Override
	public void read(DataInputStream in) {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutputStream out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleCreateParty(this);
	}

}
