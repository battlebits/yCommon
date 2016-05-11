package br.com.battlebits.ycommon.bungee.networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import br.com.battlebits.ycommon.bungee.networking.client.CommonClient;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class CommonServer implements Runnable {

	private ServerSocket server;
	private Socket client;
	public static final int PORT = 57966;
	public static final String ADDRESS = "localhost";
	private static HashMap<String, CommonClient> serverClients = new HashMap<>();

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
			if(server.isClosed())
				try {
					server.bind(new InetSocketAddress(ADDRESS, PORT));
				} catch (IOException e1) {
					e1.printStackTrace();
					break;
				}
			try {
				client = server.accept();
				new BungeeClient(client);
			} catch (IOException e) {
				BattlebitsAPI.debug("SOCKET FOI FECHADO");
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
		if (client != null)
			client.close();
		client = null;
		if (server != null)
			server.close();
		server = null;
	}

	public static void registerClient(CommonClient client) {
		if (serverClients.containsKey(client.getServerIp())) {
			serverClients.get(client.getServerIp()).disconnect();
		}
		serverClients.put(client.getServerIp(), client);
	}

	public static void disconnectClient(CommonClient client) {
		if (!serverClients.containsKey(client.getServerIp()))
			return;
		serverClients.remove(client.getServerIp());
	}

	public CommonClient getClient(String serverIp) {
		return serverClients.get(serverIp);
	}

}
