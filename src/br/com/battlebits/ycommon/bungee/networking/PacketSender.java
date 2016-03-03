package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataOutputStream;

import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class PacketSender {
	private DataOutputStream outputStream;
	
	public PacketSender(DataOutputStream outputStream) {
		this.outputStream = outputStream;
	}
	public void sendPacket(CommonPacket packet) throws Exception {
		outputStream.writeByte(packet.id());
		packet.write(outputStream);
		outputStream.flush();
	}
}
