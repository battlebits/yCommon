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
import br.com.battlebits.ycommon.bukkit.util.PluginUpdater;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.networking.packets.CPacketServerNameRequest;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;
	private static String SERVERNAME = "";

	private BukkitAccount accountManager;
	private PermissionManager permissionManager;
	private TagManager tagManager;

	private BukkitCommandLoader bukkitCommandLoader;
	private BukkitCommandFramework bukkitCommandFramework;

	private BattleBoard battleBoard;

	private MenuTranslationInjector menuTranslationInjector;

	private BukkitClient socketClient;
	
	private boolean restart;

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
		try {
			loadTranslations();
		} catch (IOException e) {
			e.printStackTrace();
		}

		socketClient.sendPacket(new CPacketServerNameRequest(getServer().getIp() + ":" + getServer().getPort()));
		
		registerCommonManagement();
		enableCommonManagement();
		registerListeners();
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		bukkitCommandFramework = new BukkitCommandFramework(this);
		bukkitCommandLoader = new BukkitCommandLoader(bukkitCommandFramework);
		bukkitCommandLoader.loadCommandsFromPackage("br.com.battlebits.ycommon.bukkit.commands.register");
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
		socketClient.disconnect();
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
		BattlePlayer player = null;
		for (Player p : getServer().getOnlinePlayers()) {
			player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			Translate.getTranslation(player.getLanguage(), messageId);
		}
		player = null;
	}

	public BukkitClient getClient() {
		return socketClient;
	}

	public BukkitAccount getAccountManager() {
		return accountManager;
	}

	public BattleBoard getBattleBoard() {
		return battleBoard;
	}

	public BukkitCommandLoader getBukkitCommandLoader() {
		return bukkitCommandLoader;
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