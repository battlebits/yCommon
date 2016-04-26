package br.com.battlebits.ycommon.bukkit.tag;

import org.bukkit.ChatColor;

import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public enum Tag {

	DONO(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "%owner%" + ChatColor.DARK_RED, Group.DONO, false), //
	ESTRELA(ChatColor.DARK_BLUE.toString() + ChatColor.BOLD + "%star%" + ChatColor.DARK_BLUE, Group.DONO, false), //
	ADMIN(ChatColor.RED.toString() + ChatColor.BOLD + "%admin%" + ChatColor.RED, Group.ADMIN, false), //
	STREAMER(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%streamer%" + ChatColor.DARK_PURPLE, Group.STREAMER, false), //
	MOD(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%mod%" + ChatColor.DARK_PURPLE, Group.MOD, false), //
	TRIAL(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "%trial%" + ChatColor.DARK_PURPLE, Group.TRIAL, false), //
	HELPER(ChatColor.BLUE + "" + ChatColor.BOLD + "%helper%" + ChatColor.BLUE, Group.HELPER, false), //
	STAFF(ChatColor.YELLOW + "" + ChatColor.BOLD + "%staff%" + ChatColor.YELLOW, Group.HELPER, false), //
	BUILDER(ChatColor.YELLOW + "" + ChatColor.BOLD + "%builder%" + ChatColor.YELLOW, Group.BUILDER, true), //
	DEV(ChatColor.DARK_AQUA.toString() + ChatColor.BOLD + "%developer%" + ChatColor.DARK_AQUA, Group.DEV, true), //
	YOUTUBER(ChatColor.AQUA + "" + ChatColor.BOLD + "%youtuber%" + ChatColor.AQUA, Group.YOUTUBER, true), //
	ULTIMATE(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "ULTIMATE" + ChatColor.LIGHT_PURPLE, Group.ULTIMATE, false), //
	PREMIUM(ChatColor.GOLD + "" + ChatColor.BOLD + "PREMIUM" + ChatColor.GOLD, Group.PREMIUM, false), //
	LIGHT(ChatColor.GREEN + "" + ChatColor.BOLD + "LIGHT" + ChatColor.GREEN, Group.LIGHT, false), //
	TORNEIO(ChatColor.DARK_BLUE + "" + ChatColor.BOLD + "%tournament%" + ChatColor.DARK_BLUE, Group.ADMIN, false), //
	WINNER(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "%winner%" + ChatColor.DARK_GREEN, Group.ADMIN, false), //
	NORMAL(ChatColor.GRAY + "", Group.NORMAL, false);

	private String prefix;
	private boolean isExclusive;
	private Group groupToUse;

	private Tag(String prefix, Group toUse, boolean exclusive) {
		this.prefix = prefix;
		this.groupToUse = toUse;
		this.isExclusive = exclusive;
	}

	public Group getGroupToUse() {
		return groupToUse;
	}

	public boolean isExclusive() {
		return isExclusive;
	}

	public String getPrefix(Language language) {
		String tag = prefix;
		for (String strTag : new String[] { "owner", "star", "admin", "streamer", "mod", "trial", "helper", "staff", "builder", "developer",
				"youtuber", "tournament", "winner" })
			tag = tag.replace("%" + strTag + "%", Translate.getTranslation(language, strTag).toUpperCase());
		return tag;
	}
}
