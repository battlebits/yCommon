package br.com.battlebits.ycommon.bukkit;

import java.io.IOException;
import java.net.UnknownHostException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitAccount;
import br.com.battlebits.ycommon.bukkit.api.inventory.menu.MenuListener;
import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.commands.CommandFramework;
import br.com.battlebits.ycommon.bukkit.commands.CommandLoader;
import br.com.battlebits.ycommon.bukkit.event.UpdateScheduler;
import br.com.battlebits.ycommon.bukkit.listeners.ChatListener;
import br.com.battlebits.ycommon.bukkit.listeners.PlayerListener;
import br.com.battlebits.ycommon.bukkit.networking.BukkitHandler;
import br.com.battlebits.ycommon.bukkit.networking.PacketSender;
import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.networking.CommonHandler;
import br.com.battlebits.ycommon.common.networking.packets.CPacketTranslationsRequest;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;
	private static String SERVERNAME = "";
	private CommandFramework commandFramework;

	private BukkitAccount accountManager;
	private PermissionManager permissionManager;
	private CommonHandler packetHandler;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {
		BattlebitsAPI.setBattleInstance(BattleInstance.BUKKIT);
	}

	@Override
	public void onEnable() {
		packetHandler = new BukkitHandler();
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, BattlebitsAPI.getBungeeChannel());
		this.getServer().getMessenger().registerIncomingPluginChannel(this, BattlebitsAPI.getBungeeChannel(), new MessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		try {
			loadTranslations();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * try { PacketSender.sendPacketReturn(new CPacketServerNameRequest(),
		 * packetHandler); } catch (Exception e) { e.printStackTrace(); }
		 */
		registerCommonManagement();
		enableCommonManagement();
		registerListeners();
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		commandFramework = new CommandFramework(this);
		new CommandLoader(commandFramework).registerAbilityListeners();
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
			try {
				PacketSender.sendPacketReturn(new CPacketTranslationsRequest(lang), packetHandler);
			} catch (Exception e) {
				e.printStackTrace();
			}
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

	public CommonHandler getPacketHandler() {
		return packetHandler;
	}

	public BukkitAccount getAccountManager() {
		return accountManager;
	}

	public static BukkitMain getPlugin() {
		return plugin;
	}

	public static ServerType getServerType() {
		return null;
	}

	public static void setServerName(String serverName) {
		SERVERNAME = serverName;
	}

	public static String getServerName() {
		return SERVERNAME;
	}

}
