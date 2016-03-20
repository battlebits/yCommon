package br.com.battlebits.ycommon.bukkit.listeners;

import java.util.Iterator;

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
import br.com.battlebits.ycommon.bukkit.event.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;

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

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onChat(AsyncPlayerChatEvent event) {
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		String format = null;
		for (Player r : Bukkit.getOnlinePlayers()) {
			BukkitPlayer receiver = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(r.getUniqueId());
			format = "";
			if (player.getActualClan() == null) {
				format = player.getTag().getPrefix(receiver.getLanguage()) + " " + ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.GOLD + ">> " + ChatColor.WHITE;
			} else {
				format = "[" + player.getActualClan().getAbbreviation() + "] " + player.getTag().getPrefix(receiver.getLanguage()) + " " + ChatColor.WHITE + player.getUserName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.GOLD + ">> " + ChatColor.WHITE;
			}
			r.sendMessage(format + event.getMessage());
		}
		BukkitMain.getPlugin().getLogger().info("<" + player.getUserName() + "> " + event.getMessage());
		event.setCancelled(true);
		format = null;
		player = null;
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.TICK)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		if (players.hasNext()) {
			BattlePlayer player = players.next();
			if (Bukkit.getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
				}
			}
		}
	}

}
