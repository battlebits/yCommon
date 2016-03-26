package br.com.battlebits.ycommon.bukkit.accounts;

import br.com.battlebits.ycommon.common.account.AccountConfiguration;

public class BukkitConfiguration extends AccountConfiguration {

	private BukkitPlayer player;
	
	public BukkitConfiguration(BukkitPlayer player) {
		this.player = player;
		super.setIgnoreAll(player.getConfiguration().isIgnoreAll());
		super.setTellEnabled(player.getConfiguration().isTellEnabled());
	}
	
	@Override
	public void setIgnoreAll(boolean ignoreAll) {
		super.setIgnoreAll(ignoreAll);
		try {
			player.updateConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setTellEnabled(boolean tellEnabled) {
		super.setTellEnabled(tellEnabled);
		try {
			player.updateConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
