package br.com.battlebits.ycommon.bungee.networking.client;

import java.io.DataInputStream;
import java.io.IOException;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.exception.HandlePacketException;
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
			byte ID;
			try {
				ID = STREAM.readByte();

				final CommonPacket PACKET = CommonPacket.get(ID);
				if (PACKET != null) {
					PACKET.read(STREAM);
					CLIENT.read++;
					PACKET.handle(CLIENT.getPacketHandler());
					BattlebitsAPI.debug("MCC>INP>" + PACKET.getClass().getName());
				}
			} catch (IOException e) {
				if (BattlebitsAPI.debugModeEnabled())
					e.printStackTrace();
				CLIENT.disconnect(true);
			} catch (HandlePacketException e) {
				e.printStackTrace();
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