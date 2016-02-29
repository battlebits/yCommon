package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class PlayerListener implements Listener {

	// [CLAN] RANK Nick (LigaSymbol) >>
	// [TEMPO] DONO GustavoInacio (*) >>

	@EventHandler(ignoreCancelled = true)
	public void onPreProcessCommand(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/me ")) {
			event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode utilizar o comando 'me'");
			event.setCancelled(true);
		}
		if (event.getMessage().split(" ")[0].contains(":")) {
			event.getPlayer().sendMessage(ChatColor.RED + "Voce nao pode enviar comando que possuem ':' (dois pontos)");
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		String format = "";
		if (player.getActualClan() == null) {
			format = player.getTag().getPrefix() + " " + ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.GOLD + ">>" + ChatColor.WHITE + " %2$s";
		} else {
			format = "[" + player.getActualClan().getAbbreviation() + "] " + player.getTag().getPrefix() + " " + ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.GOLD + ">>" + ChatColor.WHITE + " %2$s";
		}
		event.setFormat(format);
		format = null;
		player = null;
	}

}
