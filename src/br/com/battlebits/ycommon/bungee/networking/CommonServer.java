package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.networking.CommonPacket;

public class CommonServer implements Runnable {

	private ServerSocket server;
	private Socket client;
	public static final int PORT = 57966;
	public static final String ADDRESS = "localhost";

	private static boolean RUNNING = false;

	public CommonServer() throws Exception {
		server = new ServerSocket();
		server.bind(new InetSocketAddress(ADDRESS, PORT));
		RUNNING = true;
		BattlebitsAPI.debug("SERVER SOCKET > LIGADO");
	}

	@Override
	public void run() {
		while (RUNNING) {
			try {
				client = server.accept();

				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

				byte ID = inputStream.readByte();
				CommonPacket PACKET = CommonPacket.get(ID);
				if (PACKET != null) {
					BattlebitsAPI.debug("SOCKET>INP>" + PACKET.getClass().getSimpleName());
					PACKET.read(inputStream);
					PACKET.handle(new BungeePacketHandler(new PacketSender(outputStream)));
				}
				BattlebitsAPI.debug("SOCKET > CLOSE");
				outputStream.close();
				inputStream.close();
				client.close();

				outputStream = null;
				inputStream = null;
				client = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			stopServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() throws IOException {
		RUNNING = false;
		if (server != null)
			server.close();
		server = null;
		if (client != null)
			client.close();
		client = null;
	}

}
