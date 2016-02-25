package br.com.battlebits.ycommon.common.account;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.com.battlebits.ycommon.common.account.battlecraft.BattlecraftStatus;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.account.hungergames.HGStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.payment.constructors.Expire;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BattlePlayer {

	private String userName;
	private UUID uuid;
	private String fakeName;

	private List<String> nameHistory;

	private int fichas;
	private int money;
	private int xp;
	private Liga liga;
	private InetSocketAddress ipAddress;
	private String connectionFrom;

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

	private Clan actualClan;
	private Party actualParty;
	private String skype;
	private boolean friendOnly;
	private String twitter;
	private String youtubeChannel;
	private String countryCode;
	private String steam;
	private Language language;

	private HGStatus hungerGamesStatus;
	private BattlecraftStatus battlecraftStatus;
	private GameStatus gameStatus;
	private BanHistory banHistory;

	private boolean loaded;

	public BattlePlayer() {
		loaded = false;
	}

	public BattlePlayer(String userName, UUID uuid, String fakeName, List<String> nameHistory, int fichas, int money, int xp, Liga liga, InetSocketAddress ipAddress, long onlineTime, long lastLoggedIn, long firstTimePlaying, String lastIpAddress, boolean ignoreAll, Map<ServerType, Group> groups, Map<Group, Expire> ranks, Map<UUID, Friend> friends, Map<UUID, Request> friendRequests, Map<UUID, Blocked> blockedPlayers, Clan actualClan, Party actualParty, String skype, boolean friendOnly, String twitter, String youtubeChannel, String countryCode, Language language, HGStatus hungerGamesStatus, BattlecraftStatus battlecraftStatus, GameStatus gameStatus, BanHistory banHistory) {
		this.userName = userName;
		this.uuid = uuid;
		this.fakeName = fakeName;
		this.nameHistory = nameHistory;
		this.fichas = fichas;
		this.money = money;
		this.xp = xp;
		this.liga = liga;
		this.ipAddress = ipAddress;
		this.onlineTime = onlineTime;
		this.lastLoggedIn = lastLoggedIn;
		this.firstTimePlaying = firstTimePlaying;
		this.lastIpAddress = lastIpAddress;
		this.ignoreAll = ignoreAll;
		this.groups = groups;
		this.ranks = ranks;
		this.friends = friends;
		this.friendRequests = friendRequests;
		this.blockedPlayers = blockedPlayers;
		this.actualClan = actualClan;
		this.actualParty = actualParty;
		this.skype = skype;
		this.friendOnly = friendOnly;
		this.twitter = twitter;
		this.youtubeChannel = youtubeChannel;
		this.countryCode = countryCode;
		this.language = language;
		this.hungerGamesStatus = hungerGamesStatus;
		this.battlecraftStatus = battlecraftStatus;
		this.gameStatus = gameStatus;
		this.banHistory = banHistory;
		this.loaded = true;
	}

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

	public InetSocketAddress getIpAddress() {
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

	public boolean isLoaded() {
		return loaded;
	}

}
