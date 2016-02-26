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

	// INFORMAÇOES DA CONTA
	private String userName;
	private UUID uuid;
	private String fakeName;

	// DADOS DA CONTA
	private int fichas;
	private int money;
	private int xp;
	private Liga liga;

	// ENDEREÇOS E NETWORKING
	private InetSocketAddress ipAddress;
	private InetSocketAddress hostname;
	private String lastIpAddress;

	// PLAYING
	private long onlineTime;
	private long lastLoggedIn;
	private long firstTimePlaying;

	// GRUPOS
	private Map<ServerType, Group> groups;
	private Map<Group, Expire> ranks;

	// AMIGOS
	private Map<UUID, Friend> friends;
	private Map<UUID, Request> friendRequests;
	private Map<UUID, Blocked> blockedPlayers;

	// CLANS E PARTY
	private Clan actualClan;
	private Party actualParty;

	// DADOS PESSOAS COMPARTILHADOS
	private String skype;
	private boolean skypeFriendOnly;
	private String twitter;
	private String youtubeChannel;
	private String steam;

	// CONFIGURACOES
	private boolean ignoreAll;

	// PAIS E LINGUA
	private String countryCode;
	private Language language;

	// STATUS
	private HGStatus hungerGamesStatus;
	private BattlecraftStatus battlecraftStatus;
	private GameStatus gameStatus;

	// HISTORIA
	private List<String> nameHistory;
	private BanHistory banHistory;

	public BattlePlayer() {
		// TODO Auto-generated constructor stub
	}
	
	public BattlePlayer(String userName, UUID uuid, String fakeName, int fichas, int money, int xp, Liga liga, InetSocketAddress ipAddress, String lastIpAddress, InetSocketAddress hostname, long onlineTime, long lastLoggedIn, long firstTimePlaying, boolean ignoreAll, Map<ServerType, Group> groups, Map<Group, Expire> ranks, Map<UUID, Friend> friends, Map<UUID, Request> friendRequests, Map<UUID, Blocked> blockedPlayers, Clan actualClan, Party actualParty, String skype, boolean skypeFriendOnly, String twitter, String youtubeChannel, String steam, String countryCode, Language language, HGStatus hungerGamesStatus, BattlecraftStatus battlecraftStatus, GameStatus gameStatus, BanHistory banHistory, List<String> nameHistory) {
		this.userName = userName;
		this.uuid = uuid;
		this.fakeName = fakeName;

		this.fichas = fichas;
		this.money = money;
		this.xp = xp;
		this.liga = liga;

		this.ipAddress = ipAddress;
		this.lastIpAddress = lastIpAddress;
		this.hostname = hostname;

		this.onlineTime = onlineTime;
		this.lastLoggedIn = lastLoggedIn;
		this.firstTimePlaying = firstTimePlaying;

		this.ignoreAll = ignoreAll;

		this.groups = groups;
		this.ranks = ranks;

		this.friends = friends;
		this.friendRequests = friendRequests;
		this.blockedPlayers = blockedPlayers;

		this.actualClan = actualClan;
		this.actualParty = actualParty;

		this.skype = skype;
		this.skypeFriendOnly = skypeFriendOnly;
		this.twitter = twitter;
		this.youtubeChannel = youtubeChannel;
		this.steam = steam;

		this.countryCode = countryCode;
		this.language = language;

		this.hungerGamesStatus = hungerGamesStatus;
		this.battlecraftStatus = battlecraftStatus;
		this.gameStatus = gameStatus;

		this.nameHistory = nameHistory;
		this.banHistory = banHistory;
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

	public InetSocketAddress getHostname() {
		return hostname;
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

	public boolean isSkypeFriendOnly() {
		return skypeFriendOnly;
	}

	public String getTwitter() {
		return twitter;
	}

	public String getYoutubeChannel() {
		return youtubeChannel;
	}

	public String getSteam() {
		return steam;
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

	public void setFakeName(String fakeName) {
		this.fakeName = fakeName;
	}

	public void setFichas(int fichas) {
		this.fichas = fichas;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public void updateGroup(Map<ServerType, Group> groups) {
		this.groups = groups;
	}

	public void updateRanks(Map<Group, Expire> ranks) {
		this.ranks = ranks;
	}

	public void updateFriends(Map<UUID, Friend> friends) {
		this.friends = friends;
	}

	public void updateFriendRequests(Map<UUID, Request> friendRequests) {
		this.friendRequests = friendRequests;
	}

	public void updateBlockedPlayers(Map<UUID, Blocked> blockedPlayers) {
		this.blockedPlayers = blockedPlayers;
	}

	public void setActualClan(Clan actualClan) {
		this.actualClan = actualClan;
	}

	public void setActualParty(Party actualParty) {
		this.actualParty = actualParty;
	}

	public void setSkype(String skype) {
		this.skype = skype;
	}

	public void setSkypeFriendOnly(boolean skypeFriendOnly) {
		this.skypeFriendOnly = skypeFriendOnly;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public void setYoutubeChannel(String youtubeChannel) {
		this.youtubeChannel = youtubeChannel;
	}

	public void setSteam(String steam) {
		this.steam = steam;
	}

	public void setIgnoreAll(boolean ignoreAll) {
		this.ignoreAll = ignoreAll;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void updateHungerGamesStatus(HGStatus hungerGamesStatus) {
		this.hungerGamesStatus = hungerGamesStatus;
	}

	public void updateBattlecraftStatus(BattlecraftStatus battlecraftStatus) {
		this.battlecraftStatus = battlecraftStatus;
	}

	public void updateGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void updateBanHistory(BanHistory banHistory) {
		this.banHistory = banHistory;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UUID > " + uuid.toString());
		builder.append("USERNAME > " + userName);
		builder.append("FAKENAME > " + fakeName);

		builder.append("FICHAS > " + fakeName);
		builder.append("MONEY > " + fakeName);
		builder.append("XP > " + fakeName);
		builder.append("LIGA > " + fakeName);

		builder.append("ADDRESSIP > " + fakeName);
		builder.append("HOSTNAME > " + fakeName);
		builder.append("LASTADDRESSIP > " + fakeName);

		builder.append("ONLINETIME > " + fakeName);
		builder.append("LASTLOGIN > " + fakeName);
		builder.append("FIRSTJOIN > " + fakeName);

		builder.append("GROUPS > " + fakeName);
		builder.append("RANKS > " + fakeName);

		builder.append("FRIENDS > " + fakeName);
		builder.append("FRIENDSREQUEST > " + fakeName);
		builder.append("BLOCKEDFRIENDS > " + fakeName);

		builder.append("CLAN > " + fakeName);
		builder.append("PARTY > " + fakeName);

		builder.append("SKYPE > " + fakeName);
		builder.append("SKYPEFRIENDSONLY > " + fakeName);
		builder.append("TWITTER > " + fakeName);
		builder.append("YOUTUBECHANNEL > " + fakeName);
		builder.append("STEAM > " + fakeName);
		builder.append("IGNOREALL > " + fakeName);

		builder.append("COUNTRY > " + fakeName);
		builder.append("LANGUAGE > " + fakeName);

		builder.append("HGSTATUS > " + fakeName);
		builder.append("PVPSTATUS > " + fakeName);
		builder.append("GAMESTATUS > " + fakeName);

		builder.append("BANHISTORY > " + fakeName);
		builder.append("NAMEHISTORY > " + fakeName);

		return super.toString();
	}

}
