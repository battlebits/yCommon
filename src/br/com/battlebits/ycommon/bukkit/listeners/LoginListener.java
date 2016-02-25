package br.com.battlebits.ycommon.bukkit.listeners;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.minecraft.util.com.google.gson.Gson;

public class LoginListener implements Listener {

	@EventHandler
	public void onAsync(AsyncPlayerPreLoginEvent event) throws UnknownHostException, IOException {
		BattlebitsAPI.debug(System.currentTimeMillis() + "");
		Socket socket = new Socket("localhost", 57966);
		BattlebitsAPI.debug("SOCKET > CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		outputStream.writeUTF("Account");
		outputStream.writeUTF(event.getUniqueId().toString());
		outputStream.flush();
		String command = inputStream.readUTF();
		if (command.equals("Account")) {
			String json = inputStream.readUTF();
			BattlePlayer battlePlayer = new Gson().fromJson(json, BattlePlayer.class);
			BattlebitsAPI.getAccountCommon().loadBattlePlayer(event.getUniqueId(), battlePlayer);
			BattlebitsAPI.debug("NEW BATTLEPLAYER > " + battlePlayer.getUserName() + " (" + event.getUniqueId() + ")");
		}
		BattlebitsAPI.debug("SOCKET > CLOSE");
		outputStream.close();
		inputStream.close();
		socket.close();
	}

}
