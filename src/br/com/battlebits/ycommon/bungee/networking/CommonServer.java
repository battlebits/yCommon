package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class CommonServer implements Runnable {

	private ServerSocket server;
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
				Socket client = server.accept();

				DataInputStream inputStream = new DataInputStream(client.getInputStream());
				DataOutputStream outputStream = new DataOutputStream(client.getOutputStream());

				String command = inputStream.readUTF();
				String subComand = null;
				switch (command) {
				case "Account":
					subComand = inputStream.readUTF();
					UUID uuid = UUID.fromString(inputStream.readUTF());
					switch (subComand) {
					case "Load":
						BungeePacketHandler.handleAccountRequest(uuid, outputStream);
						break;
					case "Update":
						break;
					default:
						break;
					}
					uuid = null;
					break;
				case "Translations":
					subComand = inputStream.readUTF();
					switch (subComand) {
					case "Load":
						BungeePacketHandler.handleTranslationsLoad(Language.valueOf(inputStream.readUTF()), outputStream);
						break;
					default:
						break;
					}
					break;
				default:
					break;
				}
				BattlebitsAPI.debug("SOCKET > CLOSE");
				outputStream.close();
				inputStream.close();
				client.close();

				subComand = null;
				command = null;
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
		server.close();
		server = null;
	}

}
