package br.com.battlebits.yutils.common.permissions.enums;

import br.com.battlebits.yutils.common.permissions.groups.GroupInterface;
import br.com.battlebits.yutils.common.permissions.groups.SimpleGroup;

public enum Group {
	NORMAL, //
	LIGHT, //
	PREMIUM, //
	ULTIMATE, //
	YOUTUBER, //
	BUILDER, //
	STAFF, //
	HELPER, //
	DEV, //
	TRIAL, //
	MOD, //
	STREAMER, //
	ADMIN, //
	DONO;

	private GroupInterface group;

	private Group() {
		this.group = new SimpleGroup();
	}

	private Group(GroupInterface group) {
		this.group = group;
	}

	public GroupInterface getGroup() {
		return group;
	}
}
