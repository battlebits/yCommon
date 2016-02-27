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

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		Player p = event.getPlayer();
		BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
		Mute mute = player.getBanHistory().getActualMute();
		if (mute == null)
			return;
		String message = "";
		if (mute.isPermanent()) {
			message = Translate.getTranslation(player.getLanguage(), "muted-permanent");
			// VOCE FOI MUTADO PERMANENTE POR %muted-By% DURANTE %duration%. Motivo: %reason%.
			// COMPRE UNMUTE EM %store%;
		} else {
			message = Translate.getTranslation(player.getLanguage(), "muted-temp");
			// VOCE FOI MUTADO TEMPORARIAMENTE POR %muted-By% DURANTE %duration%. Motivo: %reason%.
		}
		message = message.replace("%duration%", DateUtils.formatDifference(player.getLanguage(), (mute.getDuration() - System.currentTimeMillis()) / 1000));
		message = message.replace("%forum%", BattlebitsAPI.FORUM_WEBSITE);
		message = message.replace("%store%", BattlebitsAPI.STORE);
		message = message.replace("%muted-By%", mute.getMutedBy());
		message = message.replace("%reason%", mute.getReason());
		p.sendMessage(message);
		event.setCancelled(true);
		message = null;
		mute = null;
		p = null;
		player = null;
	}
}
