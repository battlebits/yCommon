package br.com.battlebits.ycommon.common.networking.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
	public void read(DataInputStream in) throws IOException {
		command = in.readUTF();
	}

	@Override
	public void write(DataOutputStream out) throws IOException {
		out.writeUTF(command);
	}

	@Override
	public void handle(CommonHandler handler) throws HandlePacketException {
		handler.handleCommandRun(this);
	}

}
