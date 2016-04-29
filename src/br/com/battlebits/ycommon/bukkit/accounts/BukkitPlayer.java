package br.com.battlebits.ycommon.bukkit.accounts;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;

import br.com.battlebits.ycommon.bukkit.BukkitMain;
import br.com.battlebits.ycommon.bukkit.event.account.update.PlayerChangeTagEvent;
import br.com.battlebits.ycommon.bukkit.networking.PacketSender;
import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.networking.packets.CPacketAccountConfiguration;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeAccount;
import br.com.battlebits.ycommon.common.networking.packets.CPacketChangeTag;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.translate.Translate;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitPlayer extends BattlePlayer {

	private UUID lastTellUUID;
	private ArrayList<Tag> tags;

	public BukkitPlayer() {
	}

	@Override
	public boolean setTag(Tag tag) {
		PlayerChangeTagEvent event = new PlayerChangeTagEvent(Bukkit.getPlayer(getUuid()), getTag(), tag);
		BukkitMain.getPlugin().getServer().getPluginManager().callEvent(event);
		if (!event.isCancelled()) {
			super.setTag(tag);
			try {
				PacketSender.sendPacket(new CPacketChangeTag(getUuid(), tag));
			} catch (Exception e) {
				Bukkit.getPlayer(getUuid()).sendMessage(Translate.getTranslation(getLanguage(), "command-tag-prefix") + " " + Translate.getTranslation(getLanguage(), "error-try-again-please"));
				return false;
			}
		}
		return !event.isCancelled();
	}

	@Override
	public void setFakeName(String fakeName) {
		super.setFakeName(fakeName);
	}

	@Override
	public void setFichas(int fichas) {
		super.setFichas(fichas);
		try {
			PacketSender.sendPacket(new CPacketChangeAccount(this));
		} catch (Exception ex) {
			Bukkit.getPlayer(getUuid()).sendMessage(Translate.getTranslation(getLanguage(), "profile-fail-save"));
		}
	}

	@Override
	public void setMoney(int money) {
		super.setMoney(money);
		try {
			PacketSender.sendPacket(new CPacketChangeAccount(this));
		} catch (Exception ex) {
			Bukkit.getPlayer(getUuid()).sendMessage(Translate.getTranslation(getLanguage(), "profile-fail-save"));
		}
	}

	@Override
	public void setXp(int xp) {
		super.setXp(xp);
		try {
			PacketSender.sendPacket(new CPacketChangeAccount(this));
		} catch (Exception ex) {
			Bukkit.getPlayer(getUuid()).sendMessage(Translate.getTranslation(getLanguage(), "profile-fail-save"));
		}
	}

	@Override
	public void updateFriends(Map<UUID, Friend> friends) {
		super.updateFriends(friends);
	}

	@Override
	public void updateFriendRequests(Map<UUID, Request> friendRequests) {
		super.updateFriendRequests(friendRequests);
	}

	@Override
	public void updateBlockedPlayers(Map<UUID, Blocked> blockedPlayers) {
		super.updateBlockedPlayers(blockedPlayers);
	}

	@Override
	public void setActualClan(Clan actualClan) {
		super.setActualClan(actualClan);
	}

	@Override
	public void setActualParty(Party actualParty) {
		super.setActualParty(actualParty);
	}

	@Override
	public void setSkype(String skype) {
		super.setSkype(skype);
	}

	@Override
	public void setSkypeFriendOnly(boolean skypeFriendOnly) {
		super.setSkypeFriendOnly(skypeFriendOnly);
	}

	@Override
	public void setTwitter(String twitter) {
		super.setTwitter(twitter);
	}

	@Override
	public void setYoutubeChannel(String youtubeChannel) {
		super.setYoutubeChannel(youtubeChannel);
	}

	@Override
	public void setSteam(String steam) {
		super.setSteam(steam);
	}

	@Override
	public void setLanguage(Language language) {
		super.setLanguage(language);
	}

	@Override
	public void updateGameStatus(GameStatus gameStatus) {
		super.updateGameStatus(gameStatus);
	}

	@Override
	public void updateBanHistory(BanHistory banHistory) {
		super.updateBanHistory(banHistory);
	}

	public void injectConfiguration() {
		setConfiguration(new BukkitConfiguration(this));
	}

	public void loadTags() {
		tags = new ArrayList<>();
		for (Tag t : Tag.values()) {
			if (((t.isExclusive() && ((t.getGroupToUse() == getServerGroup()) || (getServerGroup().ordinal() >= Group.ADMIN.ordinal()))) || (!t.isExclusive() && getServerGroup().ordinal() >= t.getGroupToUse().ordinal()))) {
				tags.add(t);
			}
		}
	}

	public void updateConfiguration() {
		try {
			PacketSender.sendPacket(new CPacketAccountConfiguration(getUuid(), getConfiguration()));
		} catch (Exception ex) {
			Bukkit.getPlayer(getUuid()).sendMessage(Translate.getTranslation(getLanguage(), "configuration-fail-save"));
		}
	}

	public UUID getLastTellUUID() {
		return lastTellUUID;
	}

	public void setLastTellUUID(UUID lastTellUUID) {
		this.lastTellUUID = lastTellUUID;
	}

	public boolean hasLastTell() {
		return this.lastTellUUID != null;
	}

	public ArrayList<Tag> getTags() {
		return tags;
	}

}
