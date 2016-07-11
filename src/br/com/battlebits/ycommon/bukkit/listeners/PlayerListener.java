package br.com.battlebits.ycommon.bukkit.listeners;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.api.admin.AdminMode;
import br.com.battlebits.ycommon.bukkit.api.vanish.VanishAPI;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeLeagueEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent;
import br.com.battlebits.ycommon.bukkit.event.update.UpdateEvent.UpdateType;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.networking.packets.CPacketKeepAlive;
import br.com.battlebits.ycommon.common.permissions.enums.Group;

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

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onWhitelist(PlayerCommandPreprocessEvent event) {
		if (event.getMessage().toLowerCase().startsWith("/whitelist ")) {
			if (event.getPlayer().hasPermission("minecraft.command.whitelist") || event.getPlayer().hasPermission("bukkit.command.whitelist"))
				new BukkitRunnable() {
					@Override
					public void run() {
						BukkitMain.getPlugin().setCanJoin(!Bukkit.hasWhitelist());
					}
				}.runTaskLater(BukkitMain.getPlugin(), 1);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		VanishAPI.getInstance().updateVanishToPlayer(event.getPlayer());
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		if (event.getResult() == Result.KICK_WHITELIST) {
			if(BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId()) == null)
				event.disallow(Result.KICK_OTHER, ChatColor.RED + "ERROR");
			if (BattlebitsAPI.getAccountCommon().getBattlePlayer(event.getPlayer().getUniqueId()).hasGroupPermission(Group.MODPLUS)) {
				event.allow();
			}
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoinUpdate(PlayerJoinEvent event) {
		BukkitMain.getPlugin().sendUpdate();
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuitUpdate(PlayerQuitEvent event) {
		BukkitMain.getPlugin().sendUpdate(Bukkit.getOnlinePlayers().size() - 1);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		AdminMode.getInstance().removeAdmin(event.getPlayer());
		VanishAPI.getInstance().removeVanish(event.getPlayer());
		if (BukkitMain.isMemoryRamRestart())
			if (Bukkit.getOnlinePlayers().size() - 1 <= 10)
				Bukkit.shutdown();
	}

	@EventHandler
	public void onUpdate(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		Iterator<BattlePlayer> players = BattlebitsAPI.getAccountCommon().getPlayers().iterator();
		while (players.hasNext()) {
			BattlePlayer player = players.next();
			if (Bukkit.getPlayer(player.getUuid()) == null) {
				if (player.isCacheExpired()) {
					players.remove();
					BattlebitsAPI.debug("REMOVENDO BATTLEPLAYER " + player.getUserName() + " DO CACHE");
				}
			}
		}
	}

	@EventHandler
	public void onRamRestart(UpdateEvent event) {
		if (event.getType() != UpdateType.MINUTE)
			return;
		BukkitMain.isMemoryRamOnLimit();
	}

	@EventHandler
	public void onChangeLiga(PlayerChangeLeagueEvent event) {
		HashMap<String, String> replaces = new HashMap<>();
		replaces.put("%league%", event.getNewLeague().toString());
		replaces.put("%symbol%", event.getNewLeague().getSymbol());
		event.getBukkitPlayer().sendMessage("league-prefix", "league-rank-level-up", replaces);
	}

	@EventHandler
	public void onKeepAlive(UpdateEvent event) {
		if (event.getType() != UpdateType.SECOND)
			return;
		if (BukkitMain.getPlugin().getClient().keepAlive >= 30) {
			BukkitMain.getPlugin().getClient().sendPacket(new CPacketKeepAlive());
		}
		--BukkitMain.getPlugin().getClient().keepAlive;
	}

}
