package br.com.battlebits.ycommon.bukkit.tagmanager;

import org.bukkit.ChatColor;

public enum Tag {
	DONO(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "DONO " + ChatColor.DARK_RED), //
	ESTRELA(ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "ESTRELA " + ChatColor.DARK_BLUE), //
	ADMIN(ChatColor.RED.toString() + ChatColor.BOLD + "ADMIN " + ChatColor.RED), //
	STREAMER(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "STREAMER " + ChatColor.DARK_PURPLE), //
	MOD(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "MOD " + ChatColor.DARK_PURPLE), //
	TRIAL(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "TRIAL " + ChatColor.DARK_PURPLE), //
	HELPER(ChatColor.BLUE + "" + ChatColor.BOLD + "HELPER " + ChatColor.BLUE), //
	STAFF(ChatColor.YELLOW + "" + ChatColor.BOLD + "STAFF " + ChatColor.YELLOW), //
	BUILDER(ChatColor.YELLOW + "" + ChatColor.BOLD + "BUILDER " + ChatColor.YELLOW), //
	DEV(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "DEV " + ChatColor.DARK_AQUA), //
	YOUTUBER(ChatColor.AQUA + "" + ChatColor.BOLD + "YOUTUBER " + ChatColor.AQUA), //
	ULTIMATE(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "ULTIMATE " + ChatColor.LIGHT_PURPLE), //
	PREMIUM(ChatColor.GOLD + "" + ChatColor.BOLD + "PREMIUM " + ChatColor.GOLD), //
	LIGHT(ChatColor.GREEN + "" + ChatColor.BOLD + "LIGHT " + ChatColor.GREEN), //
	TORNEIO(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "TORNEIO " + ChatColor.DARK_BLUE), //
	WINNER(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "WINNER " + ChatColor.DARK_GREEN), //
	NORMAL(ChatColor.GRAY + "");

	private String prefix;

	private Tag(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
}
