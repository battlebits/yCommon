package br.com.battlebits.ycommon.bungee;

import br.com.battlebits.ycommon.bungee.listeners.LoginListener;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeMain extends Plugin {

	@Override
	public void onLoad() {

	}

	@Override
	public void onEnable() {
		loadListeners();
	}

	@Override
	public void onDisable() {

	}

	private void loadListeners() {
		getProxy().getPluginManager().registerListener(this, new LoginListener());
	}

}
