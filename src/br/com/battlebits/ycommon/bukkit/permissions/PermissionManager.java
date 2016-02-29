package br.com.battlebits.ycommon.bukkit.permissions;

import br.com.battlebits.ycommon.bukkit.BukkitCommon;
import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.permissions.injector.PermissionMatcher;
import br.com.battlebits.ycommon.bukkit.permissions.injector.RegExpMatcher;
import br.com.battlebits.ycommon.bukkit.permissions.injector.regexperms.RegexPermissions;
import br.com.battlebits.ycommon.bukkit.permissions.listeners.LoginListener;
import br.com.battlebits.ycommon.common.enums.ServerType;

public class PermissionManager extends BukkitCommon {

	private RegexPermissions regexPerms;
	protected PermissionMatcher matcher = new RegExpMatcher();
	protected LoginListener superms;

	public PermissionManager(BukkitMain main) {
		super(main);
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(superms = new LoginListener(this), getPlugin());
		regexPerms = new RegexPermissions(this);
	}

	@Override
	public void onDisable() {
		if (this.regexPerms != null) {
			this.regexPerms.onDisable();
			this.regexPerms = null;
		}
		if (this.superms != null) {
			this.superms.onDisable();
			this.superms = null;
		}
	}

	public RegexPermissions getRegexPerms() {
		return regexPerms;
	}

	public ServerType getServerType() {
		return ServerType.NETWORK;
	}

	public PermissionMatcher getPermissionMatcher() {
		return this.matcher;
	}

}
