package br.com.battlebits.ycommon.common.permissions.enums;

import br.com.battlebits.ycommon.common.permissions.groups.GroupInterface;
import br.com.battlebits.ycommon.common.permissions.groups.ModeratorGroup;
import br.com.battlebits.ycommon.common.permissions.groups.OwnerGroup;
import br.com.battlebits.ycommon.common.permissions.groups.SimpleGroup;
import br.com.battlebits.ycommon.common.permissions.groups.StreamerGroup;
import br.com.battlebits.ycommon.common.tag.Tag;

public enum Group {

	NORMAL(Tag.NORMAL), //
	LIGHT(Tag.LIGHT), //
	PREMIUM(Tag.PREMIUM), //
	ULTIMATE(Tag.ULTIMATE), //
	YOUTUBER(Tag.YOUTUBER), //
	BUILDER(Tag.BUILDER), //
	STAFF(Tag.STAFF), //
	HELPER(Tag.HELPER), //
	DEV(Tag.DEV), //
	TRIAL(Tag.TRIAL, new ModeratorGroup()), //
	MOD(Tag.MOD, new ModeratorGroup()), //
	STREAMER(Tag.STREAMER, new StreamerGroup()), //
	MANAGER(Tag.MANAGER, new StreamerGroup()), //
	ADMIN(Tag.ADMIN, new StreamerGroup()), //
	DONO(Tag.DONO, new OwnerGroup());

	private GroupInterface group;
	private Tag defaultTag;

	private Group(Tag defaultTag) {
		this(defaultTag, new SimpleGroup());
	}

	private Group(Tag defaultTag, GroupInterface group) {
		this.group = group;
		this.defaultTag = defaultTag;
	}

	public GroupInterface getGroup() {
		return group;
	}

	public Tag getDefaultTag() {
		return defaultTag;
	}

}
