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
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import net.md_5.bungee.BungeeCord;

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
		event.setCancelled(true);
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId());
		for (Player r : Bukkit.getOnlinePlayers()) {
			try {
				BukkitPlayer receiver = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(r.getUniqueId());
				if (receiver == null) {
					new BukkitRunnable() {
						@Override
						public void run() {
							r.kickPlayer("BUG");
						}
					}.runTask(BukkitMain.getPlugin());
					continue;
				}
				if ((!receiver.getConfiguration().isIgnoreAll()) && (!receiver.getBlockedPlayers().containsKey(player.getUuid()) && (!player.getBlockedPlayers().containsKey(receiver.getUuid())))) {
					String tag = player.getTag().getPrefix(receiver.getLanguage());
					String format = tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : "") + event.getPlayer().getName() + ChatColor.GRAY + " (" + player.getLiga().getSymbol() + ChatColor.GRAY + ") " + ChatColor.WHITE + ": ";
					if (player.getActualClan() != null) {
						format = "[" + player.getActualClan().getAbbreviation() + "] " + format;
					}
					r.sendMessage(format + event.getMessage());
					format = null;
				}
				receiver = null;
			} catch (Exception e) {
			}
		}
		BukkitMain.getPlugin().getLogger().info("<" + player.getUserName() + "> " + event.getMessage());
		player = null;
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		if (players.hasNext()) {
			BattlePlayer player = players.next();
			if (BungeeCord.getInstance().getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}
	}
	
}
