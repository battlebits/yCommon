package br.com.battlebits.ycommon.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.battlebits.ycommon.bukkit.bungee.MessageListener;
import br.com.battlebits.ycommon.bukkit.listeners.LoginListener;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class BukkitMain extends JavaPlugin {

	private static BukkitMain plugin;

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
		registerListeners();
	}

	@Override
	public void onDisable() {

	}

	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new LoginListener(), this);
	}

	public static BukkitMain getPlugin() {
		return plugin;
	}

}
