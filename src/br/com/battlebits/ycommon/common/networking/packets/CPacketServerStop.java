package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketServerStop extends CommonPacket {

	public CPacketServerStop() {
	}

	@Override
	public void read(DataInputStream in) {
	}

	@Override
	public void write(DataOutputStream out) {
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleServerStop(this);
	}

}
