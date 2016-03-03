package br.com.battlebits.ycommon.bukkit.accounts;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.networking.PacketSender;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountRequest;

public class BukkitAccount extends BukkitCommon {

	public BukkitAccount(BukkitMain main) {
		super(main);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			try {
				loadPlayer(p.getUniqueId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		registerListener(new AccountListener());
	}

	@Override
	public void onDisable() {
	}

	public void loadPlayer(UUID uuid) throws Exception {
		PacketSender.sendPacket(new CPacketAccountRequest(uuid), BukkitMain.getPlugin().getPacketHandler());
		Socket socket = new Socket(CommonServer.ADDRESS, CommonServer.PORT);
		BattlebitsAPI.debug("SOCKET > CONNECT");
		DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
		DataInputStream inputStream = new DataInputStream(socket.getInputStream());
		outputStream.writeUTF("Account");
		outputStream.writeUTF("Load");
		outputStream.writeUTF(uuid.toString());
		outputStream.flush();
		BattlebitsAPI.debug("SOCKET > MESSAGE SENT");
		BattlebitsAPI.debug("SOCKET > MESSAGE WAITING");
		String command = inputStream.readUTF();
		BattlebitsAPI.debug("SOCKET > MESSAGE RECEIVED");
		if (command.equals("Account")) {
			String json = inputStream.readUTF();
			BukkitPlayer battlePlayer = BukkitMain.getGson().fromJson(json, BukkitPlayer.class);
			BattlebitsAPI.getAccountCommon().loadBattlePlayer(uuid, battlePlayer);
			BattlebitsAPI.debug("NEW BATTLEPLAYER > " + battlePlayer.getUserName() + " (" + uuid + ")");
			battlePlayer = null;
			json = null;
		}
		BattlebitsAPI.debug("SOCKET > CLOSE");
		outputStream.close();
		inputStream.close();
		socket.close();
		command = null;
		outputStream = null;
		inputStream = null;
		socket = null;
	}

}
