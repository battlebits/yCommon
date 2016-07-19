package br.com.battlebits.ycommon.common.account;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import br.com.battlebits.ycommon.common.BattlebitsAPI;
import br.com.battlebits.ycommon.common.account.game.GameStatus;
import br.com.battlebits.ycommon.common.banmanager.history.BanHistory;
import br.com.battlebits.ycommon.common.clans.Clan;
import br.com.battlebits.ycommon.common.enums.AccountUpdateVersion;
import br.com.battlebits.ycommon.common.enums.Liga;
import br.com.battlebits.ycommon.common.enums.ServerStaff;
import br.com.battlebits.ycommon.common.enums.ServerType;
import br.com.battlebits.ycommon.common.enums.Torneio;
import br.com.battlebits.ycommon.common.friends.Friend;
import br.com.battlebits.ycommon.common.friends.block.Blocked;
import br.com.battlebits.ycommon.common.friends.request.Request;
import br.com.battlebits.ycommon.common.party.Party;
import br.com.battlebits.ycommon.common.payment.RankType;
import br.com.battlebits.ycommon.common.permissions.enums.Group;
import br.com.battlebits.ycommon.common.tag.Tag;
import br.com.battlebits.ycommon.common.time.TimeZone;
import br.com.battlebits.ycommon.common.time.TimeZoneConversor;
import br.com.battlebits.ycommon.common.translate.languages.Language;

public class BattlePlayer {

	// INFORMAÇOES DA CONTA
	private String userName;
	private UUID uuid;
	private String fakeName;

	// DADOS DA CONTA
	private int fichas = 0;
	private int money = 0;
	private int xp = 0;
	private Liga liga = Liga.UNRANKED;
	private Tag tag;
	private Torneio torneio = null;
	private AccountUpdateVersion accountVersion = AccountUpdateVersion.NONE;

	// ENDEREÇOS E NETWORKING
	private InetSocketAddress ipAddress;
	private String lastIpAddress;

	// PLAYING
	private long onlineTime;
	private long joinTime;
	private long lastLoggedIn;
	private long firstTimePlaying;
	private transient long cacheExpire;

	// GRUPOS
	private Map<ServerStaff, Group> groups;
	private Map<RankType, Long> ranks;

	// AMIGOS
	private Map<UUID, Friend> friends;
	private Map<UUID, Request> friendRequests;
	private Map<UUID, Blocked> blockedPlayers;

	// CLANS E PARTY
	private String clanName;
	private Party actualParty;

	// DADOS PESSOAIS COMPARTILHADOS
	private String skype;
	private boolean skypeFriendOnly;
	private String twitter;
	private String youtubeChannel;
	private String steam;

	// CONFIGURACOES
	private AccountConfiguration configuration;

	// PAIS E LINGUA
	private String countryCode;
	private Language language;
	private TimeZone timeZone;

	// STATUS
	private GameStatus gameStatus;

	// HISTORIA
	private BanHistory banHistory;

	private boolean online;

	private String serverConnected;
	private ServerType serverConnectedType;

	public BattlePlayer() {

	}

	public BattlePlayer(String userName, UUID uuid, InetSocketAddress ipAddress, String countryCode, String timeZoneCode) {
		this.userName = userName;
		this.uuid = uuid;
		this.fakeName = userName;

		this.fichas = 0;
		this.money = 0;
		this.xp = 0;
		this.liga = Liga.UNRANKED;

		this.ipAddress = ipAddress;
		if (ipAddress != null)
			this.lastIpAddress = ipAddress.getHostString();

		this.onlineTime = 0;
		this.lastLoggedIn = TimeZoneConversor.getCurrentMillsTimeIn(TimeZone.GMT0);
		this.firstTimePlaying = TimeZoneConversor.getCurrentMillsTimeIn(TimeZone.GMT0);

		this.configuration = new AccountConfiguration();

		this.groups = new HashMap<>();
		this.ranks = new HashMap<>();

		this.friends = new HashMap<>();
		this.friendRequests = new HashMap<>();
		this.blockedPlayers = new HashMap<>();

		this.clanName = "";
		this.actualParty = null;

		this.skype = "";
		this.skypeFriendOnly = true;
		this.twitter = "";
		this.youtubeChannel = "";
		this.steam = "";

		this.countryCode = countryCode;
		this.language = BattlebitsAPI.getDefaultLanguage();
		this.timeZone = TimeZone.fromString(timeZoneCode);

		this.gameStatus = new GameStatus();

		this.banHistory = new BanHistory();

		this.serverConnected = "";
		this.serverConnectedType = ServerType.NONE;

		this.tag = Tag.valueOf(getServerGroup().toString());
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

	public Torneio getTorneio() {
		return torneio;
	}

	public InetSocketAddress getIpAddress() {
		return ipAddress;
	}

	public long getOnlineTime() {
		return (System.currentTimeMillis() - joinTime) + onlineTime;
	}

	public String getHostname() {
		return ipAddress.getHostName();
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

	public AccountConfiguration getConfiguration() {
		return configuration;
	}

	public Map<ServerStaff, Group> getGroups() {
		return groups;
	}

	public Group getServerGroup() {
		Group group = Group.NORMAL;
		if (!getGroups().isEmpty()) {
			if (getGroups().containsKey(ServerType.NETWORK.getStaffType())) {
				group = getGroups().get(ServerType.NETWORK.getStaffType());
			} else if (getGroups().containsKey(serverConnectedType.getStaffType())) {
				group = getGroups().get(serverConnectedType.getStaffType());
			}
		}
		if (group == Group.NORMAL)
			if (isStaff())
				group = Group.STAFF;
		if (group == Group.NORMAL) {
			if (!getRanks().isEmpty()) {
				RankType expire = null;
				for (Entry<RankType, Long> expireRank : getRanks().entrySet()) {
					if (expire == null) {
						expire = expireRank.getKey();
					} else if (expireRank.getKey().ordinal() > expire.ordinal()) {
						expire = expireRank.getKey();
					}
				}
				if (expire != null)
					group = Group.valueOf(expire.name());
			}
		}
		return group;
	}

	public boolean hasGroupPermission(Group group) {
		if (getServerGroup() == Group.YOUTUBERPLUS) {
			return Group.MOD.ordinal() >= group.ordinal();
		}
		return getServerGroup().ordinal() >= group.ordinal();
	}

	public boolean isStaff() {
		for (Group group : getGroups().values()) {
			if (group.ordinal() > Group.HELPER.ordinal()) {
				return true;
			}
		}
		return false;
	}

	public Map<RankType, Long> getRanks() {
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

	public Clan getClan() {
		if (clanName == null || clanName.isEmpty())
			return null;
		Clan clan = BattlebitsAPI.getClanCommon().getClan(clanName);
		if (clan == null)
			clanName = "";
		return clan;
	}

	public String getClanName() {
		return clanName;
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

	public GameStatus getGameStatus() {
		if (gameStatus == null)
			gameStatus = new GameStatus();
		return gameStatus;
	}

	public BanHistory getBanHistory() {
		return banHistory;
	}

	public boolean isCacheExpired() {
		return System.currentTimeMillis() > cacheExpire;
	}

	public boolean isOnline() {
		return online;
	}

	public String getServerConnected() {
		return serverConnected;
	}

	public ServerType getServerConnectedType() {
		return serverConnectedType;
	}

	public void setFakeName(String fakeName) {
		this.fakeName = fakeName;
	}

	public void setFichas(int fichas) {
		this.fichas = fichas;
	}

	public int addFichas(int fichas) {
		this.fichas += fichas;
		setFichas(this.fichas);
		return this.fichas;
	}

	public int removeFichas(int fichas) {
		this.fichas -= fichas;
		if (this.fichas < 0)
			this.fichas = 0;
		setFichas(this.fichas);
		return this.fichas;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int addMoney(int money) {
		int multiplier = 1 + getLiga().ordinal() + (hasGroupPermission(Group.ULTIMATE) ? 1 : 0);
		int plus = money * multiplier;
		this.money += plus;
		setMoney(this.money);
		return plus;
	}

	public int removeMoney(int money) {
		this.money -= money;
		if (this.money < 0)
			this.money = 0;
		setMoney(this.money);
		return this.money;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public int addXp(int xp) {
		if (xp < 0)
			xp = 0;
		this.xp += xp;
		if (getClan() != null)
			getClan().addXp(xp);
		setXp(this.xp);
		return xp;
	}

	public AccountUpdateVersion getAccountVersion() {
		return accountVersion;
	}

	public void setAccountVersion(AccountUpdateVersion accountVersion) {
		this.accountVersion = accountVersion;
	}

	public void updateGroup(Map<ServerStaff, Group> groups) {
		this.groups = groups;
	}

	public void updateRanks(Map<RankType, Long> ranks) {
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

	public void setClan(String clanName) {
		this.clanName = clanName;
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

	public void setLiga(Liga liga) {
		this.liga = liga;
	}

	public void setTorneio(Torneio torneio) {
		this.torneio = torneio;
	}

	public void setConfiguration(AccountConfiguration config) {
		this.configuration = config;
	}

	public void setGameStatus(GameStatus status) {
		this.gameStatus = status;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void updateGameStatus(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	public void updateBanHistory(BanHistory banHistory) {
		this.banHistory = banHistory;
	}

	public void sendMessage(String translateId) {
		this.sendMessage(null, translateId);
	}

	public void sendMessage(String tagId, String translateId) {
		this.sendMessage(tagId, translateId, null);
	}

	public void sendMessage(String translateId, Map<String, String> replaces) {
		this.sendMessage(null, translateId, replaces);
	}

	public void sendMessage(String tagId, String translateId, Map<String, String> replaces) {

	}

	public void connect(String serverIp) {
		checkRanks();
		this.serverConnected = serverIp;
		this.serverConnectedType = ServerType.getServerType(serverIp);
	}

	public void updateCache() {
		this.cacheExpire = System.currentTimeMillis() + (60 * 5 * 1000);
	}

	public void setJoinData(String userName, InetSocketAddress ipAdrress, String countryCode, String timeZoneCode) {
		checkRanks();
		this.userName = userName;
		this.ipAddress = ipAdrress;
		this.timeZone = TimeZone.fromString(timeZoneCode);
		joinTime = System.currentTimeMillis();
		this.countryCode = countryCode;
		this.online = true;
		this.serverConnectedType = ServerType.NONE;
	}

	public void setLeaveData() {
		this.online = false;
		lastLoggedIn = System.currentTimeMillis();
		onlineTime = getOnlineTime();
		actualParty = null;
		if (ipAddress != null)
			lastIpAddress = ipAddress.getHostString();
		ipAddress = null;
	}

	public Tag getTag() {
		return tag;
	}

	public boolean setTag(Tag tag) {
		this.tag = tag;
		return true;
	}

	public void checkRanks() {
		if (!getRanks().isEmpty()) {
			Iterator<Entry<RankType, Long>> it = getRanks().entrySet().iterator();
			while (it.hasNext()) {
				Entry<RankType, Long> entry = it.next();
				if (TimeZoneConversor.getCurrentMillsTimeIn(TimeZone.GMT0) > entry.getValue())
					it.remove();
			}
		}
	}

	public TimeZone getTimeZone() {
		if (timeZone == null)
			setTimeZone(BattlebitsAPI.getDefaultTimeZone());
		return timeZone;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UUID > " + uuid.toString());
		builder.append(" | ");
		builder.append("USERNAME > " + userName);
		builder.append(" | ");
		builder.append("FAKENAME > " + fakeName);
		builder.append(" | ");

		builder.append("FICHAS > " + fichas);
		builder.append(" | ");
		builder.append("MONEY > " + money);
		builder.append(" | ");
		builder.append("XP > " + xp);
		builder.append(" | ");
		builder.append("LIGA > " + liga.toString());
		builder.append(" | ");
		builder.append("TAG > " + tag.toString());
		builder.append(" | ");

		builder.append("ADDRESSIP > " + ipAddress.getHostString());
		builder.append(" | ");
		builder.append("HOSTNAME > " + ipAddress.getHostName());
		builder.append(" | ");
		builder.append("LASTADDRESSIP > " + lastIpAddress);
		builder.append(" | ");

		builder.append("ONLINETIME > " + onlineTime);
		builder.append(" | ");
		builder.append("JOINTIME > " + joinTime);
		builder.append(" | ");
		builder.append("LASTLOGIN > " + lastLoggedIn);
		builder.append(" | ");
		builder.append("FIRSTJOIN > " + firstTimePlaying);
		builder.append(" | ");

		builder.append("GROUPS > " + groups);
		builder.append(" | ");
		builder.append("RANKS > " + ranks);
		builder.append(" | ");

		builder.append("FRIENDS > " + friends);
		builder.append(" | ");
		builder.append("FRIENDSREQUEST > " + friendRequests);
		builder.append(" | ");
		builder.append("BLOCKEDFRIENDS > " + blockedPlayers);
		builder.append(" | ");

		builder.append("CLAN > " + clanName);
		builder.append(" | ");
		builder.append("PARTY > " + actualParty);
		builder.append(" | ");

		builder.append("SKYPE > " + skype);
		builder.append(" | ");
		builder.append("SKYPEFRIENDSONLY > " + skypeFriendOnly);
		builder.append(" | ");
		builder.append("TWITTER > " + twitter);
		builder.append(" | ");
		builder.append("YOUTUBECHANNEL > " + youtubeChannel);
		builder.append(" | ");
		builder.append("STEAM > " + steam);
		builder.append(" | ");
		builder.append("CONFIGURATION > " + configuration.toString());
		builder.append(" | ");

		builder.append("COUNTRY > " + countryCode);
		builder.append(" | ");
		builder.append("LANGUAGE > " + language);
		builder.append(" | ");
		builder.append("TIMEZONE > " + timeZone);
		builder.append(" | ");

		builder.append("GAMESTATUS > " + gameStatus);
		builder.append(" | ");

		builder.append("BANHISTORY > " + banHistory);

		return super.toString();
	}

}
