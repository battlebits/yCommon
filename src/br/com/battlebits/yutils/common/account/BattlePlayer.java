package br.com.battlebits.yutils.common.account;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.yutils.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.yutils.common.account.game.GameStatus;
import br.com.battlebits.yutils.common.account.hungergames.HGStatus;
import br.com.battlebits.yutils.common.banmanager.history.BanHistory;
import br.com.battlebits.yutils.common.clans.Clan;
import br.com.battlebits.yutils.common.enums.Liga;
import br.com.battlebits.yutils.common.enums.ServerType;
import br.com.battlebits.yutils.common.friends.Friend;
import br.com.battlebits.yutils.common.friends.block.Blocked;
import br.com.battlebits.yutils.common.friends.request.Request;
import br.com.battlebits.yutils.common.party.Party;
import br.com.battlebits.yutils.common.payment.constructors.Expire;
import br.com.battlebits.yutils.common.permissions.enums.Group;
import br.com.battlebits.yutils.common.tagmanager.enums.Tag;
import br.com.battlebits.yutils.common.translate.languages.Language;

public class BattlePlayer {

	private String userName;
	private UUID uuid;
	private String fakeName;

	private List<String> nameHistory;

	private int fichas;
	private int money;
	private int xp;
	private Liga liga;
	private Tag tag;
	private String ipAddress;

	private long onlineTime;
	private long lastLoggedIn;
	private long firstTimePlaying;
	private String lastIpAddress;

	private boolean ignoreAll;

	private Map<ServerType, Group> groups;
	private Map<Group, Expire> ranks;
	private Map<UUID, Friend> friends;
	private Map<UUID, Request> friendRequests;
	private Map<UUID, Blocked> blockedPlayers;

	private String name;
	private Clan actualClan;
	private Party actualParty;
	private String skype;
	private boolean friendOnly;
	private String twitter;
	private String youtubeChannel;
	private String countryCode;
	private Language language;

	private HGStatus hungerGamesStatus;
	private BattlecraftStatus battlecraftStatus;
	private GameStatus gameStatus;
	private BanHistory banHistory;

	public String getUserName() {
		return userName;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getFakeName() {
		return fakeName;
	}

	public List<String> getNameHistory() {
		return nameHistory;
	}

	public int getFichas() {
		return fichas;
	}

	public int getMoney() {
		return money;
	}

	public int getXp() {
		return xp;
	}

	public Liga getLiga() {
		return liga;
	}

	public Tag getTag() {
		return tag;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public long getOnlineTime() {
		return onlineTime;
	}

	public long getLastLoggedIn() {
		return lastLoggedIn;
	}

	public long getFirstTimePlaying() {
		return firstTimePlaying;
	}

	public String getLastIpAddress() {
		return lastIpAddress;
	}

	public boolean isIgnoreAll() {
		return ignoreAll;
	}

	public Map<ServerType, Group> getGroups() {
		return groups;
	}

	public Map<Group, Expire> getRanks() {
		return ranks;
	}

	public Map<UUID, Friend> getFriends() {
		return friends;
	}

	public Map<UUID, Request> getFriendRequests() {
		return friendRequests;
	}

	public Map<UUID, Blocked> getBlockedPlayers() {
		return blockedPlayers;
	}

	public String getName() {
		return name;
	}

	public Clan getActualClan() {
		return actualClan;
	}

	public Party getActualParty() {
		return actualParty;
	}

	public String getSkype() {
		return skype;
	}

	public boolean isFriendOnly() {
		return friendOnly;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getYoutubeChannel() {
		return youtubeChannel;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public Language getLanguage() {
		return language;
	}

	public HGStatus getHungerGamesStatus() {
		return hungerGamesStatus;
	}

	public BattlecraftStatus getBattlecraftStatus() {
		return battlecraftStatus;
	}

	public GameStatus getGameStatus() {
		return gameStatus;
	}

	public BanHistory getBanHistory() {
		return banHistory;
	}

}
