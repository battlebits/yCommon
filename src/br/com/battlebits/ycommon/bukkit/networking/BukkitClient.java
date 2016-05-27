package br.com.battlebits.ycommon.bukkit.networking;

import java.net.Socket;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bungee.networking.client.CommonClient;

public class BukkitClient extends CommonClient {

	public BukkitClient(Socket socket) throws Exception {
		super(socket);
		setPacketHandler(new BukkitHandler());
	}

	@Override
	public void disconnect(boolean removeServer) {
		super.disconnect(removeServer);
		if (!removeServer)
			BukkitMain.getPlugin().reconnect();
	}

}
