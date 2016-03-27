package br.com.battlebits.ycommon.bukkit.accounts;

import br.com.battlebits.ycommon.common.account.AccountConfiguration;

public class BukkitConfiguration extends AccountConfiguration {

	private transient BukkitPlayer player;

	public BukkitConfiguration(BukkitPlayer player) {
		this.player = player;
		super.setIgnoreAll(player.getConfiguration().isIgnoreAll());
		super.setTellEnabled(player.getConfiguration().isTellEnabled());
	}

	@Override
	public void setIgnoreAll(boolean ignoreAll) {
		super.setIgnoreAll(ignoreAll);
		player.updateConfiguration();
	}

	@Override
	public void setTellEnabled(boolean tellEnabled) {
		super.setTellEnabled(tellEnabled);
		player.updateConfiguration();
	}

}
