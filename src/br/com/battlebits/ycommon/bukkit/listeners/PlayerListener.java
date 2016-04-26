package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.common.BattlebitsAPI;

public class PlayerListener implements Listener {

	// [CLAN] RANK Nick (LigaSymbol) >>
	// [TEMPO] DONO GustavoInacio (*) >>

	@EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
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

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onChat(AsyncPlayerChatEvent event) {
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		for (Player r : Bukkit.getOnlinePlayers()) {
			BukkitPlayer receiver = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(r.getUniqueId());
			if ((!receiver.getConfiguration().isIgnoreAll()) && (!receiver.getBlockedPlayers().containsKey(player.getUuid())
					&& (!player.getBlockedPlayers().containsKey(receiver.getUuid())))) {
				String format = "";
				if (player.getActualClan() == null) {
					format = player.getTag().getPrefix(receiver.getLanguage()) + " " + ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " ("
							+ player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.GOLD + ">> " + ChatColor.WHITE;
				} else {
					format = "[" + player.getActualClan().getAbbreviation() + "] " + player.getTag().getPrefix(receiver.getLanguage()) + " "
							+ ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") "
							+ ChatColor.GOLD + ">> " + ChatColor.WHITE;
				}
				r.sendMessage(format + event.getMessage());
				format = null;
			}
			receiver = null;
		}
		BukkitMain.getPlugin().getLogger().info("<" + player.getUserName() + "> " + event.getMessage());
		event.setCancelled(true);
		player = null;
	}

}
