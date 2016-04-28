package br.com.battlebits.ycommon.bukkit.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class BattleScoreboard {

	public Team createTeam(Player p, String id, String prefix, String suffix) {
		Team t = p.getScoreboard().getTeam(id);
		if (t == null) {
			t = p.getScoreboard().registerNewTeam(id);
		}
		t.setPrefix(prefix);
		t.setSuffix(suffix);
		return t;
	}

	public void joinTeam(Player p, Player join, String id) {
		Team t = p.getScoreboard().getTeam(id);
		if (t == null) {
			t = createTeam(p, id, "", "");
		}
		leaveTeam(p, join);
		t.addEntry(join.getName());
		t = null;
	}

	public void leaveTeam(Player p, Player leave) {
		Team t = p.getScoreboard().getPlayerTeam(leave);
		if (t != null) {
			t.removeEntry(leave.getName());
			if (t.getSize() == 0) {
				t.unregister();
			}
			t = null;
		}
	}

}
