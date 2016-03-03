package br.com.battlebits.ycommon.bukkit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitAccount;
import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.event.UpdateScheduler;
import br.com.battlebits.ycommon.bukkit.listeners.ChatListener;
import br.com.battlebits.ycommon.bukkit.listeners.PlayerListener;
import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.reflect.TypeToken;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;
	private static Gson gson = new Gson();

	private BukkitAccount accountManager;
	private PermissionManager permissionManager;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {
		BattlebitsAPI.setBattleInstance(BattleInstance.BUKKIT);
	}

	@Override
	public void onEnable() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, BattlebitsAPI.getBungeeChannel());
		this.getServer().getMessenger().registerIncomingPluginChannel(this, BattlebitsAPI.getBungeeChannel(), new MessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		try {
			loadTranslations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		registerCommonManagement();
		enableCommonManagement();
		registerListeners();
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1);
	}

	@Override
	public void onDisable() {
		accountManager.onDisable();
		permissionManager.onDisable();
		accountManager = null;
		permissionManager = null;
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
	}

	private void registerCommonManagement() {
		accountManager = new BukkitAccount(this);
		permissionManager = new PermissionManager(this);
	}

	private void loadTranslations() throws UnknownHostException, IOException {
		for (Language lang : Language.values()) {
			Socket socket = new Socket(CommonServer.ADDRESS, CommonServer.PORT);
			BattlebitsAPI.debug("SOCKET > CONNECT");
			DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
			DataInputStream inputStream = new DataInputStream(socket.getInputStream());
			outputStream.writeUTF("Translations");
			outputStream.writeUTF("Load");
			outputStream.writeUTF(lang.toString());
			outputStream.flush();
			BattlebitsAPI.debug("SOCKET > MESSAGE SENT");
			BattlebitsAPI.debug("SOCKET > MESSAGE WAITING");
			String command = inputStream.readUTF();
			BattlebitsAPI.debug("SOCKET > MESSAGE RECEIVED");
			if (command.equals("Translations")) {
				String json = inputStream.readUTF();
				HashMap<String, String> translation = getGson().fromJson(json, new TypeToken<HashMap<String, String>>() {
				}.getType());
				Translate.loadTranslations(lang, translation);
				BattlebitsAPI.debug("NEW TRANSLATION > " + lang);
				json = null;
			}
			command = null;

			BattlebitsAPI.debug("SOCKET > CLOSE");
			outputStream.close();
			inputStream.close();
			socket.close();
			outputStream = null;
			inputStream = null;
			socket = null;
		}
	}

	private void enableCommonManagement() {
		accountManager.onEnable();
		permissionManager.onEnable();
	}

	@SuppressWarnings("deprecation")
	public void broadcastMessage(String messageId) {
		BattlePlayer player = null;
		for (Player p : getServer().getOnlinePlayers()) {
			player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			Translate.getTranslation(player.getLanguage(), messageId);
		}
		player = null;
	}

	public BukkitAccount getAccountManager() {
		return accountManager;
	}

	public static BukkitMain getPlugin() {
		return plugin;
	}

	public static Gson getGson() {
		return gson;
	}

	public static ServerType getServerType() {
		return null;
	}

}
