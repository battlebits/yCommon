package br.com.battlebits.ycommon.bungee.networking.client;

import java.io.DataInputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CommonConnectionInput extends Thread {

	public final DataInputStream STREAM;
	private final CommonClient CLIENT;

	private boolean run = false;

	public CommonConnectionInput(CommonClient client, DataInputStream s) {
		STREAM = s;
		CLIENT = client;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			try {
				final byte ID = STREAM.readByte();
				final CommonPacket PACKET = CommonPacket.get(ID);
				if (PACKET != null) {
					PACKET.read(STREAM);
					CLIENT.read++;
					PACKET.handle(CLIENT.getPacketHandler());
					BattlebitsAPI.debug("MCC>INP>" + PACKET.getClass().getName());
				}

			} catch (Exception e) {
				e.printStackTrace();
				CLIENT.disconnect();
			}
		}
	}

	public void close() throws IOException {
		STREAM.close();
	}

	public void stopThread() {
		run = false;
	}
}