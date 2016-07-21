package br.com.battlebits.ycommon.common.clans.ranking;

import br.com.battlebits.ycommon.common.enums.Liga;
import net.md_5.bungee.api.ChatColor;

public enum ClanRank {
	INITIAL(ChatColor.WHITE, Integer.MAX_VALUE);

	private ChatColor color;
	private int maxXp;

	private ClanRank(ChatColor color, int xp) {
		this.color = color;
		this.maxXp = xp;
	}

	public int getMaxXp() {
		return maxXp;
	}

	public ChatColor getColor() {
		return color;
	}

	public Liga getNextLiga() {
		return Liga.values()[ordinal() + 1];
	}
}
