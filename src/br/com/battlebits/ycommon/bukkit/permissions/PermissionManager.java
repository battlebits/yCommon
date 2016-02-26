package br.com.battlebits.ycommon.bukkit.permissions;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.permissions.injector.PermissionMatcher;
import br.com.battlebits.ycommon.bukkit.permissions.injector.RegExpMatcher;
import br.com.battlebits.ycommon.bukkit.permissions.injector.regexperms.RegexPermissions;
import br.com.battlebits.ycommon.common.enums.ServerType;

public class PermissionManager extends BukkitCommon {

	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher = new RegExpMatcher();

	public PermissionManager(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {

	}

	@Override
	public void onDisable() {

	}

	public RegexPermissions getRegexPerms() {
		return regexPerms;
	}

	public ServerType getServerType() {
		return null;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}

}
