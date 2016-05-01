package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CPacketCommandRun extends CommonPacket {

	private String command;

	public CPacketCommandRun() {

	}

	public CPacketCommandRun(String command) {
		this.command = command;
	}

	public String getCommand() {
		return command;
	}
	
	@Override
	public void read(DataInputStream in) throws Exception {
		command = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws Exception {
		out.writeUTF(command);
	}

	@Override
	public void handle(CommonHandler handler) throws Exception {
		handler.handleCommandRun(this);
	}

}
