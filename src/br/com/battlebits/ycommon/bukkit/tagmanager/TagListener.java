package br.com.battlebits.ycommon.bukkit.tagmanager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeTagEvent;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.tag.Tag;

public class TagListener implements Listener {
	private TagManager manager;

	@SuppressWarnings("deprecation")
	public TagListener(TagManager manager) {
		this.manager = manager;
		for(Player p : manager.getServer().getOnlinePlayers()) {
			BattlePlayer player = BattlebitsAPI.getAccountCommon().getBattlePlayer(p.getUniqueId());
			player.setTag(player.getTag());
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event) {
		manager.removePlayerTag(event.getPlayer());
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();
				BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
				player.setTag(player.getDefaultTag());
				for (Player o : Bukkit.getOnlinePlayers()) {
					if (o.getUniqueId() != p.getUniqueId()) {
						BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
						String id2 = getTeamName(bp.getTag(), bp.getLiga());
						String tag = bp.getTag().getPrefix(player.getLanguage());
						manager.getPlugin().getBattleBoard().joinTeam(manager.getPlugin().getBattleBoard().createTeamIfNotExistsToPlayer(p, id2, tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : ""), " §7(" + player.getLiga().getSymbol() + "§7)"), o);
						bp = null;
					}
					o = null;
				}
				player = null;
				p = null;
			}
		}.runTaskAsynchronously(manager.getPlugin());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeTagListener(PlayerChangeTagEvent e) {
		Player p = e.getPlayer();
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
		String id = getTeamName(e.getNewTag(), player.getLiga());
		for (final Player o : Bukkit.getOnlinePlayers()) {
			try {
				BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
				if (bp == null) {
					new BukkitRunnable() {
						@Override
						public void run() {
							o.kickPlayer("BUG");
						}
					}.runTask(BukkitMain.getPlugin());
					continue;
				}
				String tag = e.getNewTag().getPrefix(bp.getLanguage());
				manager.getPlugin().getBattleBoard().joinTeam(manager.getPlugin().getBattleBoard().createTeamIfNotExistsToPlayer(o, id, tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : ""), " §7(" + player.getLiga().getSymbol() + "§7)"), p);
				bp = null;
			} catch (Exception e2) {
			}
		}
		id = null;
		player = null;
		p = null;
	}

	private static char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public static String getTeamName(Tag tag, Liga liga) {
		return chars[tag.ordinal()] + "-" + chars[Liga.values().length - liga.ordinal()];
	}

}
