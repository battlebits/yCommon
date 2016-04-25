package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.battlebits.ycommon.bungee.managers.BanManager;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;

public class ChatListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		Mute mute = player.getBanHistory().getActualMute();
		if (mute == null)
			return;
		p.sendMessage(BanManager.getMuteMessage(mute, player.getLanguage()));
		event.setCancelled(true);
		mute = null;
		p = null;
		player = null;
	}
}
