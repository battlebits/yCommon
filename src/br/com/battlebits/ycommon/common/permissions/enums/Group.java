package br.com.battlebits.ycommon.common.permissions.enums;

import br.com.battlebits.ycommon.common.permissions.groups.GroupInterface;
import br.com.battlebits.ycommon.common.permissions.groups.SimpleGroup;

public enum Group {
	NORMAL, //
	LIGHT, //
	PREMIUM, //
	ULTIMATE, //
	YOUTUBER, //
	BUILDER, //
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
