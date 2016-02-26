package br.com.battlebits.ycommon.bukkit.accounts;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;

public class BukkitAccount extends BukkitCommon {

	public BukkitAccount(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {
		registerListener(new AccountListener());
	}

	@Override
	public void onDisable() {

	}

}
