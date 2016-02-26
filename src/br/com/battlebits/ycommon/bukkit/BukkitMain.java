package br.com.battlebits.ycommon.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitAccount;
import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.event.UpdateScheduler;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.enums.ServerType;
import net.minecraft.util.com.google.gson.Gson;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;
	private static Gson gson = new Gson();

	private BukkitAccount accountManager;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, BattlebitsAPI.getBungeeChannel());
		this.getServer().getMessenger().registerIncomingPluginChannel(this, BattlebitsAPI.getBungeeChannel(), new MessageListener());
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new MessageListener());
		registerCommonManagement();
		enableCommonManagement();
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1);
	}

	@Override
	public void onDisable() {
		accountManager.onDisable();
	}

	private void registerCommonManagement() {
		accountManager = new BukkitAccount(this);
	}

	private void enableCommonManagement() {
		accountManager.onEnable();
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
