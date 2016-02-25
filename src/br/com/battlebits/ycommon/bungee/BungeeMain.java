package br.com.battlebits.ycommon.bungee;

import br.com.battlebits.ycommon.bungee.listeners.LoginListener;
import br.com.battlebits.ycommon.bungee.networking.CommonServer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {

	private static BungeeMain plugin;

	{
		plugin = this;
	}

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {
		try {
			getProxy().getScheduler().runAsync(this, new CommonServer());
		} catch (Exception e) {
			e.printStackTrace();
		}
		getProxy().registerChannel(BattlebitsAPI.getBungeeChannel());
		loadListeners();
	}

	@Override
	public void onDisable() {

	}

	private void loadListeners() {
		getProxy().getPluginManager().registerListener(this, new LoginListener());
	}

	public static BungeeMain getPlugin() {
		return plugin;
	}

}
