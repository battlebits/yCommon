package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.banmanager.constructors.Mute;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.utils.DateUtils;

public class ChatListener implements Listener {

	// [CLAN] RANK Nick (LigaSymbol) >>
	// [TEMPO] DONO GustavoInacio (*) >>

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		Mute mute = player.getBanHistory().getActualMute();
		if (mute == null)
			return;
		if (mute.isPermanent()) {

		} else {
			// VOCE FOI MUTADO POR 
			String message = Translate.getTranslation(player.getLanguage(), "muted-temp");
			message = message.replace("%duration%", DateUtils.formatDifference(player.getLanguage(), (mute.getDuration() - System.currentTimeMillis()) / 1000));
			message = message.replace("%muted-By%", mute.getMutedBy());
			message = message.replace("%reason%", mute.getReason());
			p.sendMessage(message);
		}
		event.setCancelled(true);
	}
}
