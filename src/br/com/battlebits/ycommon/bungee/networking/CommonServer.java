package br.com.battlebits.ycommon.bungee.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import com.google.gson.Gson;

import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class CommonServer implements Runnable {

	private ServerSocket server;
	private static final int PORT = 57966;
	private static final String ADDRESS = "0.0.0.0";

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
				String subCommand = inputStream.readUTF();
				switch (subCommand) {
				case "Account":
					UUID uuid = UUID.fromString(inputStream.readUTF());
					handleAccountRequest(uuid, outputStream);
					break;
				default:
					break;
				}
				BattlebitsAPI.debug("SOCKET > CLOSE");
				outputStream.close();
				inputStream.close();
				client.close();
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

	public void handleAccountRequest(UUID uuid, DataOutputStream output) throws Exception {
		output.writeUTF("Account");
		String json = new Gson().toJson(BattlebitsAPI.getAccountCommon().getBattlePlayer(uuid));
		output.writeUTF(json);
		output.flush();
	}

	public void stopServer() throws IOException {
		RUNNING = false;
		server.close();
	}

}
