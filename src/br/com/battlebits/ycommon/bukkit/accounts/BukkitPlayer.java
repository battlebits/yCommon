package br.com.battlebits.ycommon.bukkit.accounts;

import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.common.account.BattlePlayer;
import br.com.battlebits.ycommon.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.account.hungergames.HGStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BukkitPlayer extends BattlePlayer {

	@Override
	public void setFakeName(String fakeName) {
		super.setFakeName(fakeName);
	}

	@Override
	public void setFichas(int fichas) {
		super.setFichas(fichas);
	}

	@Override
	public void setMoney(int money) {
		super.setMoney(money);
	}

	@Override
	public void setXp(int xp) {
		super.setXp(xp);
	}

	@Override
	public void updateGroup(Map<ServerType, Group> groups) {
		super.updateGroup(groups);
	}

	@Override
	public void updateRanks(Map<Group, Expire> ranks) {
		super.updateRanks(ranks);
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
	public void setIgnoreAll(boolean ignoreAll) {
		super.setIgnoreAll(ignoreAll);
	}

	@Override
	public void setLanguage(Language language) {
		super.setLanguage(language);
	}

	@Override
	public void updateHungerGamesStatus(HGStatus hungerGamesStatus) {
		super.updateHungerGamesStatus(hungerGamesStatus);
	}

	@Override
	public void updateBattlecraftStatus(BattlecraftStatus battlecraftStatus) {
		super.updateBattlecraftStatus(battlecraftStatus);
	}

	@Override
	public void updateGameStatus(GameStatus gameStatus) {
		super.updateGameStatus(gameStatus);
	}

	@Override
	public void updateBanHistory(BanHistory banHistory) {
		super.updateBanHistory(banHistory);
	}

}
