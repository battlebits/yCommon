package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketRemoveBlockedPlayer extends CommonPacket {

	@Override
	public void read(DataInputStream in) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleUnblockPlayer(this);
	}

}
