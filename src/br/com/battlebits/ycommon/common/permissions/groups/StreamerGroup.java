package br.com.battlebits.ycommon.common.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class StreamerGroup extends GroupInterface {

	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<>();
		for (String str : new String[] { "stop", "summon", "setworldspawn", "time", "effect", "kick", "enchant", "give", "gamemode", "toggledownfall", "tp", "clear", "whitelist" }) {
			permissions.add("minecraft.command." + str);
			permissions.add("bukkit.command." + str);
		}
		permissions.add("worldedit.*");
		return permissions;
	}
}
