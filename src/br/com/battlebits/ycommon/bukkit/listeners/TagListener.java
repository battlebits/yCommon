package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.accounts.BukkitPlayer;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeTagEvent;
import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.tag.Tag;

public class TagListener implements Listener {

	private BukkitMain main;

	public TagListener(BukkitMain bk) {
		this.main = bk;
	}

	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Player p = e.getPlayer();
				BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
				player.setTag(player.getTag());
				for (Player o : Bukkit.getOnlinePlayers()) {
					if (o.getPlayer().getUniqueId() != e.getPlayer().getUniqueId()) {
						BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
						String id2 = getTeamName(bp.getTag(), bp.getLiga());
						String tag = bp.getTag().getPrefix(player.getLanguage());
						main.getBattleScoreboard().createTeam(p, id2, tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : ""), " §7(" + bp.getLiga().getSymbol() + "§7)");
						main.getBattleScoreboard().joinTeam(p, o, id2);
						bp = null;
					}
					o = null;
				}
				player = null;
				p = null;
			}
		}.runTaskAsynchronously(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeTagListener(PlayerChangeTagEvent e) {
		Player p = e.getPlayer();
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
		String id = getTeamName(e.getNewTag(), player.getLiga());
		for (Player o : Bukkit.getOnlinePlayers()) {
			BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
			String tag = e.getNewTag().getPrefix(bp.getLanguage());
			main.getBattleScoreboard().leaveTeam(o, p);
			main.getBattleScoreboard().createTeam(o, id, tag + (ChatColor.stripColor(tag).trim().length() > 0 ? " " : ""), " §7(" + player.getLiga().getSymbol() + "§7)");
			main.getBattleScoreboard().joinTeam(o, p, id);
			bp = null;
			o = null;
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
