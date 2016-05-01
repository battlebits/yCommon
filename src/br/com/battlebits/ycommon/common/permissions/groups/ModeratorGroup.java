package br.com.battlebits.ycommon.common.permissions.groups;

import java.util.ArrayList;
import java.util.List;

public class ModeratorGroup extends GroupInterface {

	@Override
	public List<String> getPermissions() {
		List<String> permissions = new ArrayList<>();
		for (String str : new String[] { "kick" }) {
			permissions.add("minecraft.command." + str);
			permissions.add("bukkit.command." + str);
		}
		return permissions;
	}
}
