package br.com.battlebits.ycommon.bukkit.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
		// new BukkitRunnable() {
		// @SuppressWarnings("deprecation")
		// @Override
		// public void run() {
		Player p = e.getPlayer();
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
		String id = getTeamName(player.getTag(), player.getLiga());
		main.getBattleScoreboard().createTeam(e.getPlayer(), id, player.getTag().getPrefix(player.getLanguage()) + " ", " §7(" + player.getLiga().getSymbol() + "§7)");
		main.getBattleScoreboard().joinTeam(p, p, player.getTag().ordinal() + "-" + player.getLiga().ordinal());
		for (Player o : Bukkit.getOnlinePlayers()) {
			if (o.getPlayer().getUniqueId() != e.getPlayer().getUniqueId()) {
				BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
				main.getBattleScoreboard().createTeam(e.getPlayer(), getTeamName(bp.getTag(), bp.getLiga()), bp.getTag().getPrefix(player.getLanguage()) + " ", " §7(" + bp.getLiga().getSymbol() + "§7)");
				main.getBattleScoreboard().joinTeam(e.getPlayer(), o, (bp.getTag().ordinal()) + "-" + (bp.getLiga().ordinal()));
				main.getBattleScoreboard().createTeam(o, id, player.getTag().getPrefix(bp.getLanguage()) + " ", " §7(" + player.getLiga().getSymbol() + "§7)");
				main.getBattleScoreboard().joinTeam(o, p, (Tag.values().length - bp.getTag().ordinal()) + "-" + (Liga.values().length - bp.getLiga().ordinal()));
				bp = null;
			}
			o = null;
		}
		id = null;
		player = null;
		p = null;
		// }
		// }.runTaskAsynchronously(main);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerChangeTagListener(PlayerChangeTagEvent e) {
		// new BukkitRunnable() {
		// @SuppressWarnings("deprecation")
		// @Override
		// public void run() {
		Player p = e.getPlayer();
		BukkitPlayer player = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(e.getPlayer().getUniqueId());
		String id = getTeamName(e.getNewTag(), player.getLiga());
		main.getBattleScoreboard().createTeam(p, id, e.getNewTag().getPrefix(player.getLanguage()) + " ", " §7(" + player.getLiga().getSymbol() + "§7)");
		main.getBattleScoreboard().joinTeam(p, p, id);
		for (Player o : Bukkit.getOnlinePlayers()) {
			if (p.getUniqueId() != o.getUniqueId()) {
				BukkitPlayer bp = (BukkitPlayer) BattlebitsAPI.getAccountCommon().getBattlePlayer(o.getUniqueId());
				main.getBattleScoreboard().createTeam(o, id, e.getNewTag().getPrefix(bp.getLanguage()) + " ", " §7(" + player.getLiga().getSymbol() + "§7)");
				main.getBattleScoreboard().joinTeam(o, p, id);
				bp = null;
			}
			o = null;
		}
		id = null;
		player = null;
		p = null;
		// }
		// }.runTaskAsynchronously(main);
	}

	private char[] chars = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	public String getTeamName(Tag tag, Liga liga) {
		return chars[tag.ordinal()] + "-" + chars[liga.ordinal()];
	}

}
