package br.com.battlebits.ycommon.bungee.listeners;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	@EventHandler
	public void onLogin(PreLoginEvent event) {
		BattlebitsAPI.debug("UUID > " + event.getConnection().getUniqueId().toString());
		BattlebitsAPI.debug("NAME > " + event.getConnection().getName());
	}
}
