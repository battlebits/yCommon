package br.com.battlebits.ycommon.bungee.networking.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.networking.CommonPacket;
import br.com.battlebits.ycommon.common.networking.packets.CPacketKeepAlive;

public class CommonConnectionOutput extends Thread {

	public final DataOutputStream STREAM;
	private final CommonClient CLIENT;
	private final Object LOCK = new Object();
	private final ArrayList<CommonPacket> QUEUE = new ArrayList<CommonPacket>();

	private boolean run = false;
	private boolean keep = false;
	private int keepTimer = 0;

	public CommonConnectionOutput(CommonClient client, DataOutputStream s) {
		STREAM = s;
		CLIENT = client;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			try {
				synchronized (QUEUE) {
					while (QUEUE.size() > 0) {
						final CommonPacket PACKET = QUEUE.get(0);
						STREAM.writeByte(PACKET.id());
						PACKET.write(STREAM);
						CLIENT.written++;
						QUEUE.remove(0);
						keep = true;
						BattlebitsAPI.debug("MCC>OUT>" + PACKET.getClass().getName());
					}
				}
				synchronized (LOCK) {
					LOCK.wait(3250);
				}
			} catch (Exception e) {
				if (BattlebitsAPI.debugModeEnabled()) {
					e.printStackTrace();
				}
				CLIENT.disconnect();
			}
		}
	}

	public void stopThread() {
		run = false;
	}

	public void close() throws IOException {
		STREAM.close();
	}

	public void send(CommonPacket p) {
		synchronized (QUEUE) {
			QUEUE.add(p);
		}
		synchronized (LOCK) {
			LOCK.notifyAll();
		}
	}

	public void keepAlive() {
		keepTimer--;
		if (keepTimer > 0) {
			return;
		}
		if (!keep) {
			send(new CPacketKeepAlive());
		}
		keep = false;
		keepTimer = 30;
	}

	public int queue() {
		synchronized (QUEUE) {
			return QUEUE.size();
		}
	}

}