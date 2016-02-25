package br.com.battlebits.ycommon.common.enums;

import net.md_5.bungee.api.ChatColor;

public enum Liga {
	FIRST(ChatColor.WHITE + "-", 200), //
	NOOB(ChatColor.WHITE + "=", 600), //
	PRIMARY(ChatColor.WHITE + "☰", 1400), //
	INTERMEDIARY(ChatColor.YELLOW + "☱", 2400), //
	ADVANCE(ChatColor.YELLOW + "☳", 3600), //
	EXPERT(ChatColor.YELLOW + "☷", 5000), //
	BRONZE(ChatColor.GOLD + "✶", 6400), //
	SILVER(ChatColor.GOLD + "✷", 8000), //
	GOLD(ChatColor.GOLD + "✸", 9800), //
	DIAMOND(ChatColor.RED + "✹", 11800), //
	KING(ChatColor.RED + "✫", 15000), //
	LEGENDARY(ChatColor.DARK_RED + "✪", Integer.MAX_VALUE);

	private String symbol;
	private int max;

	private Liga(String symbol, int max) {
		this.symbol = symbol;
		this.max = max;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		int min = 0;
		if (this.ordinal() > 0)
			min = Liga.values()[this.ordinal() - 1].getMax();
		return min;
	}

	public String getSymbol() {
		return symbol;
	}

	public static Liga getLiga(int xp) {
		Liga liga = FIRST;
		for (Liga rank : values()) {
			if (xp <= rank.max && xp > rank.getMin())
				liga = rank;
		}
		return liga;
	}
}
