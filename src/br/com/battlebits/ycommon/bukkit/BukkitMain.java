package br.com.battlebits.ycommon.bukkit;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitAccount;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuListener;
import br.com.battlebits.ycommon.bukkit.board.BattleBoard;
import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandFramework;
import br.com.battlebits.ycommon.bukkit.commands.BukkitCommandLoader;
import br.com.battlebits.ycommon.bukkit.injector.Injector;
import br.com.battlebits.ycommon.bukkit.injector.injectors.MenuTranslationInjector;
import br.com.battlebits.ycommon.bukkit.listeners.ChatListener;
import br.com.battlebits.ycommon.bukkit.listeners.PlayerListener;
import br.com.battlebits.ycommon.bukkit.listeners.ScoreboardListener;
import br.com.battlebits.ycommon.bukkit.menu.preferences.PreferencesMenu;
import br.com.battlebits.ycommon.bukkit.networking.BukkitClient;
import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.bukkit.run.UpdateScheduler;
import br.com.battlebits.ycommon.bukkit.tagmanager.TagManager;
import br.com.battlebits.ycommon.bukkit.utils.PluginUpdater;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.bungee.servers.HungerGamesServer.HungerGamesState;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerInfo;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerRecall;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStart;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerStop;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;
	private static String SERVERNAME = "";

	private BukkitAccount accountManager;
	private PermissionManager permissionManager;
	private TagManager tagManager;

	private BattleBoard battleBoard;

	private MenuTranslationInjector menuTranslationInjector;

	private BukkitClient socketClient;

	private boolean restart;

	private boolean canJoin = true;
	private HungerGamesState state = HungerGamesState.NONE;
	private int tempo = 0;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {
		restart = new PluginUpdater(this).run();
		BattlebitsAPI.setBattleInstance(BattleInstance.BUKKIT);
	}

	@Override
	public void onEnable() {
		if (restart)
			return;
		Injector.createTinyProtocol(this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, BattlebitsAPI.getBungeeChannel());
		this.getServer().getMessenger().registerIncomingPluginChannel(this, BattlebitsAPI.getBungeeChannel(), new MessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		battleBoard = new BattleBoard();
		try {
			Socket socket = new Socket(CommonServer.ADDRESS, CommonServer.PORT);
			socketClient = new BukkitClient(socket);
		} catch (Exception e1) {
			e1.printStackTrace();
			getServer().shutdown();
			restart = true;
			return;
		}
		socketClient.sendPacket(new CPacketServerStart(getServer().getIp() + ":" + getServer().getPort(), getServer().getMaxPlayers()));

		try {
			loadTranslations();
		} catch (IOException e) {
			e.printStackTrace();
		}

		registerCommonManagement();
		enableCommonManagement();
		registerListeners();
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		new BukkitCommandLoader(new BukkitCommandFramework(this)).loadCommandsFromPackage("br.com.battlebits.ycommon.bukkit.commands.register");
		menuTranslationInjector = new MenuTranslationInjector();
		menuTranslationInjector.inject();
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1);
		new PreferencesMenu();
	}

	@Override
	public void onDisable() {
		menuTranslationInjector.end();
		accountManager.onDisable();
		permissionManager.onDisable();
		tagManager.onDisable();
		accountManager = null;
		permissionManager = null;
		tagManager = null;
		getClient().sendPacket(new CPacketServerStop());
		socketClient.disconnect(true);
	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new ChatListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new ScoreboardListener(), this);
	}

	private void registerCommonManagement() {
		accountManager = new BukkitAccount(this);
		permissionManager = new PermissionManager(this);
		tagManager = new TagManager(this);
	}

	public void loadTranslations() throws UnknownHostException, IOException {
		for (Language lang : Language.values()) {
			socketClient.sendPacket(new CPacketTranslationsRequest(lang));
		}
	}

	private void enableCommonManagement() {
		accountManager.onEnable();
		permissionManager.onEnable();
		tagManager.onEnable();
	}

	public void broadcastMessage(String messageId) {
		broadcastMessage(null, messageId);
	}

	public void broadcastMessage(String tagPrefix, String messageId) {
		BattlePlayer player = null;
		for (Player p : getServer().getOnlinePlayers()) {
			player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			player.sendMessage(tagPrefix, messageId);
		}
		player = null;
	}

	public void sendUpdate() {
		sendUpdate(getServer().getOnlinePlayers().size());
	}

	public void sendUpdate(int online) {
		getClient().sendPacket(new CPacketServerInfo(online, getServer().getMaxPlayers(), canJoin, tempo, state));
	}

	public void setCanJoin(boolean canJoin) {
		this.canJoin = canJoin;
		sendUpdate();
	}

	public void setState(HungerGamesState state) {
		if (state == null)
			return;
		this.state = state;
		sendUpdate();
	}

	public void setTempo(int tempo) {
		this.tempo = tempo;
		sendUpdate();
	}

	public void reconnect() {
		try {
			Socket socket = new Socket(CommonServer.ADDRESS, CommonServer.PORT);
			socketClient = new BukkitClient(socket);
			socketClient.sendPacket(new CPacketServerRecall(getServer().getIp() + ":" + getServer().getPort(), getServer().getOnlinePlayers().size(), getServer().getMaxPlayers()));
		} catch (Exception e1) {
			e1.printStackTrace();
			getServer().shutdown();
		}
	}

	public BukkitClient getClient() {
		if (socketClient.socket.isClosed())
			reconnect();
		return socketClient;
	}

	public BukkitAccount getAccountManager() {
		return accountManager;
	}

	public BattleBoard getBattleBoard() {
		return battleBoard;
	}

	public static BukkitMain getPlugin() {
		return plugin;
	}

	public static ServerType getServerType() {
		return ServerType.getServerType(SERVERNAME);
	}

	public boolean isRestarting() {
		return restart;
	}

	public static void setServerName(String serverName) {
		SERVERNAME = serverName;
	}

	public static String getServerHostName() {
		return SERVERNAME;
	}

}