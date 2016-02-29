package br.com.battlebits.ycommon.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitAccount;
import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.event.UpdateScheduler;
import br.com.battlebits.ycommon.bukkit.listeners.ChatListener;
import br.com.battlebits.ycommon.bukkit.listeners.PlayerListener;
import br.com.battlebits.ycommon.bukkit.permissions.PermissionManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.BattleInstance;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.translate.Translate;
import net.minecraft.util.com.google.gson.Gson;

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
