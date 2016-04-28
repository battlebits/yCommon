package br.com.battlebits.ycommon.bukkit.event.account.update;

import org.bukkit.entity.Player;

import br.com.battlebits.ycommon.bukkit.event.PlayerCancellableEvent;
import br.com.battlebits.ycommon.bukkit.tag.Tag;

public class PlayerChangeTagEvent extends PlayerCancellableEvent {

	private Tag oldTag;
	private Tag newTag;

	public PlayerChangeTagEvent(Player p, Tag oldTag, Tag newTag) {
		super(p);
		this.oldTag = oldTag;
		this.newTag = newTag;
	}

	public Tag getNewTag() {
		return newTag;
	}

	public Tag getOldTag() {
		return oldTag;
	}

}
