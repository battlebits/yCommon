package br.com.battlebits.ycommon.common.tag;

import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public enum Tag {

	DONO("§4§l%owner%§4", Group.DONO, false), 
	ESTRELA("§1§l%star%§1", Group.DONO, false), 
	ADMIN("§c§l%admin%§c", Group.ADMIN, false), 
	MANAGER("§c§l%manager%§c", Group.MANAGER, false), 
	STREAMER("§5§l%streamer%§5", Group.STREAMER, false), 
	MOD("§5§l%mod%§5", Group.MOD, false), 
	TRIAL("§5§l%trial%§5", Group.TRIAL, false), 
	HELPER("§9§l%helper%§9", Group.HELPER, false), 
	STAFF("§e§l%staff%§e", Group.HELPER, false), 
	BUILDER("§e§l%builder%§e", Group.BUILDER, true),
	DEV("§3§l%developer%§3", Group.DEV, true), 
	YOUTUBER("§b§l%youtuber%§b", Group.YOUTUBER, true),
	ULTIMATE("§d§lULTIMATE§d", Group.ULTIMATE, false),
	PREMIUM("§6§lPREMIUM§6", Group.PREMIUM, false),
	LIGHT("§a§lLIGHT§a", Group.LIGHT, false), 
	TORNEIO("§1§l%tournament%§1", Group.ADMIN, false), 
	WINNER("§2§l%winner%§2", Group.ADMIN, false), 
	NORMAL("§7", Group.NORMAL, false);

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
		for (String strTag : new String[] { "owner", "star", "admin", "manager", "streamer", "mod", "trial", "helper", "staff", "builder",
				"developer", "youtuber", "tournament", "winner" })
			tag = tag.replace("%" + strTag + "%", Translate.getTranslation(language, strTag).toUpperCase());
		return tag;
	}
}
