package br.com.battlebits.ycommon.bukkit;

import org.bukkit.Server;

import br.com.battlebits.ycommon.common.BattleCommon;

public abstract class BukkitCommon extends BattleCommon {

	private BukkitMain main;

	public BukkitCommon(BukkitMain main) {
		this.main = main;
	}

	public BukkitMain getPlugin() {
		return main;
	}

	public Server getServer() {
		return main.getServer();
	}

}
