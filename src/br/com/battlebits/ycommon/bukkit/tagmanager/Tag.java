package br.com.battlebits.ycommon.bukkit.tagmanager;

import org.bukkit.ChatColor;

import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public enum Tag {
	DONO(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "%owner%" + ChatColor.DARK_RED), //
	ESTRELA(ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "%star%" + ChatColor.DARK_BLUE), //
	ADMIN(ChatColor.RED.toString() + ChatColor.BOLD + "%admin%" + ChatColor.RED), //
	STREAMER(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%streamer%" + ChatColor.DARK_PURPLE), //
	MOD(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%mod%" + ChatColor.DARK_PURPLE), //
	TRIAL(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%trial%" + ChatColor.DARK_PURPLE), //
	HELPER(ChatColor.BLUE + "" + ChatColor.BOLD + "%helper%" + ChatColor.BLUE), //
	STAFF(ChatColor.YELLOW + "" + ChatColor.BOLD + "%staff%" + ChatColor.YELLOW), //
	BUILDER(ChatColor.YELLOW + "" + ChatColor.BOLD + "%builder%" + ChatColor.YELLOW), //
	DEV(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "%developer%" + ChatColor.DARK_AQUA), //
	YOUTUBER(ChatColor.AQUA + "" + ChatColor.BOLD + "%youtuber%" + ChatColor.AQUA), //
	ULTIMATE(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "ULTIMATE" + ChatColor.LIGHT_PURPLE), //
	PREMIUM(ChatColor.GOLD + "" + ChatColor.BOLD + "PREMIUM" + ChatColor.GOLD), //
	LIGHT(ChatColor.GREEN + "" + ChatColor.BOLD + "LIGHT" + ChatColor.GREEN), //
	TORNEIO(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "%tournament%" + ChatColor.DARK_BLUE), //
	WINNER(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "%winner%" + ChatColor.DARK_GREEN), //
	NORMAL(ChatColor.GRAY + "");

	private String prefix;

	private Tag(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix(Language language) {
		String tag = prefix;
		for (String strTag : new String[] { "owner", "star", "admin", "streamer", "mod", "trial", "helper", "staff", "builder", "developer", "youtuber", "tournament", "winner" })
			tag = tag.replace("%" + strTag + "%", Translate.getTranslation(language, strTag).toUpperCase());
		return tag;
	}
}
